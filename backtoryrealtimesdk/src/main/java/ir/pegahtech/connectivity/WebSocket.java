package ir.pegahtech.connectivity;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;


public abstract class WebSocket {
    private static final int VERSION = 13;

    static final byte OPCODE_TEXT = 0x1;
    static final byte OPCODE_BINARY = 0x2;
    static final byte OPCODE_CLOSE = 0x8;
    static final byte OPCODE_PING = 0x9;
    static final byte OPCODE_PONG = 0xA;

    private URI uri = null;
    private WebSocketEventHandler eventHandler = null;
    private Integer heartBeat = null;

    private volatile boolean connected = false;

    private Socket socket = null;
    private DataInputStream input = null;
    private PrintStream output = null;

    private WebSocketReceiver receiver = null;
    private WebSocketHandshake handshake = null;

    private final Random random = new SecureRandom();

    private String connectivityInstanceId;
    private String token;

    private String protocol;
    private Map<String, String> extraHeaders;

    List<String> requestsList = new ArrayList();

    public WebSocket(String connectivityInstanceId, String token) throws URISyntaxException {
        this(null, null, connectivityInstanceId, token);
    }

    public WebSocket(String protocol, String connectivityInstanceId, String token) throws URISyntaxException {
        this(protocol, null, connectivityInstanceId, token);
    }

    public WebSocket(String protocol, Map<String, String> extraHeaders,
                     final String connectivityInstanceId, String token) throws URISyntaxException {
        setToken(token);
        setConnectivityInstanceId(connectivityInstanceId);
        setProtocol(protocol);
        setExtraHeaders(extraHeaders);
    }

    protected abstract URI generateURI() throws URISyntaxException;

    protected void setEventHandler(WebSocketEventHandler eventHandler) {
        this.eventHandler = eventHandler;
    }

    public WebSocketEventHandler getEventHandler() {
        return this.eventHandler;
    }

    public void connect() throws WebSocketException, URISyntaxException {
        this.uri = generateURI();
        handshake = new WebSocketHandshake(uri, protocol, extraHeaders);
        if (connectivityInstanceId == null) {
            throw new RuntimeException("Instance id should not be null");
        }
        if (token == null) {
            throw new RuntimeException("Token should not be null");
        }

        try {
            if (connected) {
                throw new WebSocketException("already connected");
            }

            try {
                socket = createSocket();
            } catch (WebSocketException e) {
                throw e;
            }

            input = new DataInputStream(socket.getInputStream());
            output = new PrintStream(socket.getOutputStream());

            if (this instanceof ConnectivityWebSocket) {
                output.write(handshake.getHandshake(connectivityInstanceId, token));
            } else {
                output.write(handshake.getHandshake(connectivityInstanceId, token,
                        ((RealtimeWebSocket)this).getRealtimeChallengeId()));
            }

            boolean handshakeComplete = false;
            int len = 1000;
            byte[] buffer = new byte[len];
            int pos = 0;
            ArrayList<String> handshakeLines = new ArrayList<String>();

            while (!handshakeComplete) {
                int b = input.read();
                buffer[pos] = (byte) b;
                pos += 1;

                if (buffer[pos - 1] == 0x0A && buffer[pos - 2] == 0x0D) {
                    String line = new String(buffer, "UTF-8");
                    if (line.trim().equals("")) {
                        handshakeComplete = true;
                    } else {
                        handshakeLines.add(line.trim());
                    }

                    buffer = new byte[len];
                    pos = 0;
                }
            }

            for (String line : handshakeLines) {
                System.out.println(line);
            }
            handshake.verifyServerStatusLine(handshakeLines.get(0));
            handshakeLines.remove(0);

            for (String line : handshakeLines) {
                String[] splitedLine = line.split(":");
                if (splitedLine[0].equals("heartbeat-period")) {
                    setHeartBeat(new Integer(splitedLine[1].substring(1)));
                    break;
                }
            }

            HashMap<String, String> headers = new HashMap<String, String>();
            for (String line : handshakeLines) {
                String[] keyValue = line.split(": ", 2);
                headers.put(keyValue[0], keyValue[1]);
            }
            handshake.verifyServerHandshakeHeaders(headers);

            receiver = new WebSocketReceiver(input, this);
            receiver.start();
            connected = true;
            eventHandler.onOpen();
        } catch (WebSocketException wse) {
            throw wse;
        } catch (IOException ioe) {
            throw new WebSocketException("error while connecting: " + ioe.getMessage(), ioe);
        }
    }

//    public synchronized void ping() throws WebSocketException {
//        if (!connected) {
//            throw new WebSocketException("error while sending text data: not connected");
//        }
//
//        try {
//            this.sendFrame(OPCODE_PING, true, new byte[0]);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    protected synchronized void send(String data) throws WebSocketException {
        if (!connected) {
            throw new WebSocketException("error while sending text data: not connected");
        }

        try {
            this.sendFrame(OPCODE_TEXT, true, data.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private synchronized void sendFrame(byte opcode, boolean masking, byte[] data) throws WebSocketException, IOException {
        int headerLength = 2; // This is just an assumed headerLength, as we use a ByteArrayOutputStream
        if (masking) {
            headerLength += 4;
        }
        ByteArrayOutputStream frame = new ByteArrayOutputStream(data.length + headerLength);

        byte fin = (byte) 0x80;
        byte startByte = (byte) (fin | opcode);
        frame.write(startByte);
        int length = data.length;
        int length_field = 0;

        if (length < 126) {
            if (masking) {
                length = 0x80 | length;
            }
            frame.write((byte) length);
        } else if (length <= 65535) {
            length_field = 126;
            if (masking) {
                length_field = 0x80 | length_field;
            }
            frame.write((byte) length_field);
            byte[] lengthBytes = intToByteArray(length);
            frame.write(lengthBytes[2]);
            frame.write(lengthBytes[3]);
        } else {
            length_field = 127;
            if (masking) {
                length_field = 0x80 | length_field;
            }
            frame.write((byte) length_field);
            // Since an integer occupies just 4 bytes we fill the 4 leading length bytes with zero
            frame.write(new byte[]{0x0, 0x0, 0x0, 0x0});
            frame.write(intToByteArray(length));
        }

        byte[] mask = null;
        if (masking) {
            mask = generateMask();
            frame.write(mask);

            for (int i = 0; i < data.length; i++) {
                data[i] ^= mask[i % 4];
            }
        }

        frame.write(data);
        output.write(frame.toByteArray());
        output.flush();
    }

    public void handleReceiverError() {
        try {
            if (connected) {
                close();
            }
        } catch (WebSocketException wse) {
            wse.printStackTrace();
        }
    }

    public synchronized void close() throws WebSocketException {
        if (!connected) {
            return;
        }

        sendCloseHandshake();

        if (receiver.isRunning()) {
            receiver.stopit();
        }

        closeStreams();

        eventHandler.onClose();
    }

    private synchronized void sendCloseHandshake() throws WebSocketException {
        if (!connected) {
            throw new WebSocketException("error while sending close handshake: not connected");
        }

        System.out.println("Sending close");
        if (!connected) {
            throw new WebSocketException("error while sending close");
        }

        try {
            this.sendFrame(OPCODE_CLOSE, true, new byte[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }

        connected = false;
    }

    private Socket createSocket() throws WebSocketException {
        String scheme = uri.getScheme();
        String host = uri.getHost();
        int port = uri.getPort();

        Socket socket = null;

        if (scheme != null && scheme.equals("ws")) {
            if (port == -1) {
                port = 80;
            }
            try {
                socket = new Socket(host, port);
            } catch (UnknownHostException uhe) {
                throw new WebSocketException("unknown host: " + host, uhe);
            } catch (IOException ioe) {
                throw new WebSocketException("error while creating socket to " + uri, ioe);
            }
        } else if (scheme != null && scheme.equals("wss")) {
            if (port == -1) {
                port = 443;
            }
            try {
                SocketFactory factory = SSLSocketFactory.getDefault();
                socket = factory.createSocket(host, port);
            } catch (UnknownHostException uhe) {
                throw new WebSocketException("unknown host: " + host, uhe);
            } catch (IOException ioe) {
                throw new WebSocketException("error while creating secure socket to " + uri, ioe);
            }
        } else {
            throw new WebSocketException("unsupported protocol: " + scheme);
        }

        return socket;
    }

    private byte[] generateMask() {
        final byte[] mask = new byte[4];
        random.nextBytes(mask);
        return mask;
    }

    private byte[] intToByteArray(int number) {
        byte[] bytes = ByteBuffer.allocate(4).putInt(number).array();
        return bytes;
    }

    private void closeStreams() throws WebSocketException {
        try {
            input.close();
            output.close();
            socket.close();
        } catch (IOException ioe) {
            throw new WebSocketException("error while closing websocket connection: ", ioe);
        }
    }

    public static int getVersion() {
        return VERSION;
    }

    public Integer getHeartBeat() {
        return heartBeat;
    }

    public void setHeartBeat(Integer heartBeat) {
        this.heartBeat = heartBeat;
    }

    public String getInstanceId() {
        return connectivityInstanceId;
    }

    public void setConnectivityInstanceId(String connectivityInstanceId) {
        this.connectivityInstanceId = connectivityInstanceId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void disconnect() {
        send("DISCONNECT\n" +
                "X-Backtory-Connectivity-Id:" + getInstanceId() + "\n" +
                "\n\0");
        // ToDo: close the socket!
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public void setExtraHeaders(Map<String, String> extraHeaders) {
        this.extraHeaders = extraHeaders;
    }
}
