module badminton.bot {
    requires annotations;
    requires log4j.api;
    requires static lombok;
    requires org.apache.commons.lang3;
    requires telegrambots.client;
    requires telegrambots.longpolling;
    requires telegrambots.meta;
    requires org.checkerframework.checker.qual;
    requires java.net.http;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires com.fasterxml.jackson.databind;
    requires java.xml;

    // export models for jackson to access
    opens amateurs.model to com.fasterxml.jackson.databind;
    exports amateurs.mapper;
}
