package boot.io.bootworkshop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/* import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
*/

@SpringBootApplication
@EnableMongoRepositories
public class BootworkshopApplication {

	@Bean
	CommandLineRunner demoData(CoffeeRepo repo) {
		return args -> {
			repo.deleteAll();

			Stream.of("Breakfast Blend", "Java", "Hazelnut", "Dark Roast")
					.map(Coffee::new)
					.forEach(repo::save);

			repo.findAll().forEach(System.out::println);
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(BootworkshopApplication.class, args);
	}

}

@RestController
class HelloController {
	@GetMapping("/hello")
	String hello() {
		return "Hello!";
	}
}

@RestController
@RequestMapping("/coffees")
class BootWorkshopController {
	@Autowired
	private CoffeeRepo repo;

	/*
	public BootWorkshopController(CoffeeRepo repo) {
		System.out.println("BootWorkshopController constructor");
		this.repo = repo;
		repo.deleteAll();

		Stream.of("Breakfast Blend", "Java", "Hazelnut", "Dark Roast")
				.map(Coffee::new)
				.forEach(repo::save);
		repo.findAll().forEach(System.out::println);
	}
	*/

	@GetMapping
	Iterable<Coffee> getAllCoffees() {
		//List<Coffee> coffees = new ArrayList<Coffee>();
		//repo.findAll().forEach(coffees::add);
		//coffees.forEach(System.out::println);
		//return coffees;
		return repo.findAll();
	}

	// search for JPA model
	/*
	@GetMapping("/{id}")
	Optional<Coffee> getCoffeeById(@PathVariable Long id) {
		return  repo.findById(id);
	}
	*/

	// search for Mongo model
	@GetMapping("/{id}")
	Optional<Coffee> getCoffeeById(@PathVariable String id) {
		System.out.println(repo.findById(id));
		return repo.findById(id);
	}

	@GetMapping("/search")
	Coffee searchForCoffee(@RequestParam String name) {
		return  repo.findByName(name);
	}
}


// Model for Mongo
@Document
class Coffee {
	public Coffee(@NotNull String name) {
		this.name = name;
	}

	public Coffee() {
		name = "Decaf";
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Id
	private String id;

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	@NotNull
	private String name;

	@Override
	public String toString() {
		return name;
	}
}

@Repository
interface CoffeeRepo extends MongoRepository<Coffee, String> {
	Coffee findByName(String name);
}


// Model for JPA
/*
@Entity
class Coffee {
	public Coffee(@NotNull String name) {
		this.name = name;
	}

	public Coffee() {
		name = "Decaf";
	}

	@Id
	@GeneratedValue
	private Long id;

	@NotNull
	private String name;
}

interface CoffeeRepo extends CrudRepository<Coffee, Long> {
	Coffee findByName(String name);
}
*/

