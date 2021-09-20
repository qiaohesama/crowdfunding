
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.SQLException;

public class Default {
    public static void main(String[] args) throws SQLException {
        System.out.println(new BCryptPasswordEncoder().encode("123123"));
    }
}
