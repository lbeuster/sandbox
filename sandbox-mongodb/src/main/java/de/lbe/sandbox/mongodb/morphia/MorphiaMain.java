package de.lbe.sandbox.mongodb.morphia;

import java.util.Collection;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Key;
import com.google.code.morphia.Morphia;
import com.mongodb.MongoClient;

import de.asideas.lib.commons.util.CollectionUtils;

/**
 * Orika says that we should use the mapper factory as a singleton.
 * 
 * http://orika-mapper.github.io/orika-docs/performance-tuning.html
 * 
 * @author lars.beuster
 */
public class MorphiaMain {

	private static final String TESTDB = "morphiadb";

	public static void main(String[] args) throws Exception {

		MongoClient mongoClient = new MongoClient("localhost", 27017);

		// delete the test db
		mongoClient.dropDatabase(TESTDB);

		Morphia morphia = new Morphia();

		Datastore ds = morphia.createDatastore(mongoClient, TESTDB);
		morphia.map(Employee.class);

		System.out.println(ds.getCount(Employee.class));

		Employee boss = new Employee();
		boss.setFirstName("boss1");
		boss.setLastName("boss-lastname");
		Key<Employee> bossId = ds.save(boss);

		// get an employee without a manager
		Employee scott = new Employee();
		scott.setFirstName("scott");
		scott.setLastName("scott-lastname");
		scott.setManager(bossId);
		Key<Employee> scottId = ds.save(scott);

		Collection<Employee> emps = CollectionUtils.toList(ds.find(Employee.class).field("manager").notEqual(null).iterator());
		System.out.println(emps);

		boss = ds.getByKey(Employee.class, bossId);
		System.out.println(boss.getUnderlings());

		boss.getUnderlings().add(scott);
		ds.save(boss);

		boss = ds.getByKey(Employee.class, bossId);
		System.out.println(boss.getUnderlings());

		/*
		 * Key<Employee> scottsKey = ds.save(new Employee("Scott", "Hernandez", ds.getKey(boss), 150**1000));
		 * 
		 * //add Scott as an employee of his manager UpdateResults<Employee> res = ds.update( boss,
		 * ds.createUpdateOperations(Employee.class).add("underlings", scottsKey) );
		 * 
		 * // get Scott's boss; the same as the one above. Employee scottsBoss = ds.find(Employee.class).filter("underlings", scottsKey).get();
		 * 
		 * for (Employee e : ds.find(Employee.class, "manager", boss)) print(e);
		 */
	}
}
