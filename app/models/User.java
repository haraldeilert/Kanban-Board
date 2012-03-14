package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import play.Play;
import play.data.validation.Required;
import play.data.validation.Unique;
import play.db.jpa.Model;
import play.libs.Codec;

@Entity
public class User extends Model {

	@Unique
	@Required
	@Column(unique = true)
	public String email;
	
	@OneToMany(mappedBy="user", cascade=CascadeType.ALL)
	public List<Board> adminBoards;

	@Required
	public String passwordHash;

	@ManyToMany(cascade=CascadeType.PERSIST)
	public List<Board> accessBoards;

	public User(String email, String password) {
		this.email = email;
		this.passwordHash = Codec.hexMD5(password);
		this.accessBoards = new ArrayList<Board>();
		this.adminBoards = new ArrayList<Board>();
	}

/*	@PreRemove
	public void preRemove() {
		for (Board b : this.otherBoards) {
			b.user.remove(this);
		}

	}*/
	
	public static User connect(String email, String password) {
        return find("byEmailAndPasswordHash", email, password).first();
    }

	public boolean checkPassword(String password) {
		return passwordHash.equals(Codec.hexMD5(password));
	}

	public boolean isAdmin() {
		return email.equals(Play.configuration.getProperty("kanbanboard.adminEmail", ""));
	}

	public static User findByEmail(String email) {
		return find("email", email).first();
	}

	public String toString() {
		return email;
	}
}