# The simpliest strategy is to not run proguard against your project's own code.
# This doesn't provide the benefits of optimization & obfuscation against your
# project, but will still strip the libraries. The advantage is that your app will
# work without any subsequent effort.
-keep class mx.x10.filipebezerra.horariosrmtcgoiania.** { *; }