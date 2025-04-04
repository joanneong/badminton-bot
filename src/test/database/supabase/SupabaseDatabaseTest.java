package database.supabase;

import amateurs.database.Database;
import amateurs.database.supabase.SupabaseDatabase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SupabaseDatabaseTest {
    private Database database;

    @BeforeEach
    public void setUp() {
        database = new SupabaseDatabase();
    }

    @Test
    public void testGetAllGames() {
        System.out.println(database.getAllGames(19704089L));
    }
}
