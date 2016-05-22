-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-verbose

-keepattributes Signature

# For using GSON @Expose annotation
-keepattributes *Annotation*

-keepattributes EnclosingMethod

-keepnames class ir.pegahtech.connectivity.** { *; }
-keep class ir.pegahtech.backtory.models.** { *; }

-keepclasseswithmembers class * {
    native <methods>;
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}
