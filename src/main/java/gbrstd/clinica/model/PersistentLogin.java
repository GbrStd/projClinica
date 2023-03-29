package gbrstd.clinica.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name = "persistent_logins")
public class PersistentLogin {

    /*
      CREATE TABLE `persistent_logins` (
      `username` VARCHAR(64) NOT NULL,
      `series` VARCHAR(64) NOT NULL,
      `token` VARCHAR(64) NOT NULL,
      `last_used` TIMESTAMP NOT NULL,
      PRIMARY KEY (`series`));
    * */

    @Column(name = "username", nullable = false, length = 64)
    private String username;

    @Column(name = "series", nullable = false, unique = true, length = 64)
    @Id
    private String series;

    @Column(name = "token", nullable = false, length = 64)
    private String token;

    @Column(name = "last_used", nullable = false)
    private Timestamp lastUsed;

}
