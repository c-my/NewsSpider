module cmy.newsspider {
    exports cmy.newsspider.record to com.fasterxml.jackson.databind;
    requires org.jsoup;
    requires jdk.crypto.ec;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
}