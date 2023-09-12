package com.mycompany;

import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.CriteriaBuilderFactory;
import com.mycompany.config.PersistenceConfig;
import com.mycompany.model.Comment;
import com.mycompany.model.Post;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

@SpringBootApplication
public class BlazeAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlazeAppApplication.class, args);
	}

	private CriteriaBuilderFactory cbf;
	private EntityManager em;
	public BlazeAppApplication(PersistenceConfig config) {
		this.cbf = config.getCbf();
		this.em = config.getEm();
	}

	void createFakeData(EntityManager em) {

		Faker faker = new Faker();
		Random random = new Random();

		em.getTransaction().begin();
		for (int i = 0; i < 10; i++) {
			Post post = new Post();
			post.setTitle(faker.artist().name());
			post.setContent(faker.marketing().buzzwords());
			post.setRating(random.nextInt(5));

			Comment comment1 = new Comment();
			comment1.setComment(faker.color().name());
			comment1.setLikes(random.nextInt(100));

			Comment comment2 = new Comment();
			comment2.setComment(faker.color().name());
			comment2.setDislikes(random.nextInt(100));

			post.setComments(new HashSet<>(Arrays.asList(comment1, comment2)));
			em.persist(post);
		}
		em.getTransaction().commit();
		em.close();
	}

	@Bean
	CommandLineRunner setup() {
		return args -> {
		//	createFakeData(em);

//			List<Post> posts = cbf.create(em, Post.class)
//					.getResultList();

//			CriteriaBuilder<Comment> subQuery = cbf.create(em, Comment.class)
//							.where("likes").ge(70).select("id");
//
//			List<Post> posts = cbf.create(em, Post.class)
//							.where("comments.id").in(subQuery).end()
//							.getResultList();
			CriteriaBuilder<Tuple> cb = cbf.create(em, Tuple.class)
					.from(Post.class, "p")
					.select("p.id")
					.select("CASE p.rating WHEN 0 THEN 'Not Good' ELSE 'Good' END");
			List<Tuple> result = cb.getResultList();

			result.forEach(p -> System.out.println(p.get(0) + " -- " + p.get(1)));
		};
	}
}
