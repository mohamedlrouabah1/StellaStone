package com.stormstars.stellastone;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.stormstars.stellastone.model.atelier.Fusee;
import com.stormstars.stellastone.model.user.User;

@SpringBootTest
class StellastoneApplicationTests {


	@Test
	void test_user() {
		User user = new User();
		user.setFirstname("firstname");
		user.setLastname("lastname");
		user.setUsername("username");
		user.setEmail("email");
		user.setPassword("password");
		user.setAvatar(new byte[0]);
		user.getUserAvatar();
	}
	@Test
	void test_fusee() {
		Fusee fusee = new Fusee();
		fusee.setNomFusee("nomFusee");
		fusee.setInfos("infos");
		fusee.setPropriete("propriete");
		fusee.setHistorique("historique");
		fusee.setImage(new byte[0]);
		fusee.getIdIconFusee();
	}

	@Test
	void test_User_Fusee() {
		User user = new User();
		Fusee fusee = new Fusee();
		fusee.setUser(user);
	}
}