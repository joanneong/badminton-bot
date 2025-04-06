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

    // export models for jackson to access
    exports amateurs.model;
    exports amateurs.mapper;
}
