package quiz.demo.data.model.support;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Calendar;

@Entity
@Table(name = "persistent_logins")
public class PersistentLogins {
	@NotNull
	@Column(name = "username", length = 64)
	String username;
	
	@NotNull
	@Id
	@Column(name = "series", length = 64)
    String series;
	
	@NotNull
	@Column(name = "token", length = 64)
    String token;
	
	@NotNull
	@Column(columnDefinition = "TIMESTAMP")
	Calendar last_used;
}
