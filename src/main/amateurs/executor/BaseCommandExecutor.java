package amateurs.executor;

import amateurs.ChatClient;
import amateurs.database.Database;
import amateurs.database.supabase.SupabaseDatabase;

public class BaseCommandExecutor {
    protected final ChatClient chatClient;

    protected final Database database;

    public BaseCommandExecutor() {
        this.chatClient = ChatClient.getInstance();
        this.database = new SupabaseDatabase();
    }
}
