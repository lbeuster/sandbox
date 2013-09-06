package de.lbe.sandbox.mongodb.morphia;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;

import com.google.code.morphia.Key;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.annotations.NotSaved;
import com.google.code.morphia.annotations.PostLoad;
import com.google.code.morphia.annotations.Property;
import com.google.code.morphia.annotations.Reference;
import com.google.code.morphia.annotations.Transient;
import com.mongodb.DBObject;

import de.asideas.lib.commons.lang.ObjectUtils;

@Entity("employees")
public class Employee {
	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public Long getSalary() {
		return salary;
	}

	public void setSalary(Long salary) {
		this.salary = salary;
	}

	public Key<Employee> getManager() {
		return manager;
	}

	public void setManager(Key<Employee> manager) {
		this.manager = manager;
	}

	public List<Employee> getUnderlings() {
		return underlings;
	}

	public void setUnderlings(List<Employee> underlings) {
		this.underlings = underlings;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getReadButNotStored() {
		return readButNotStored;
	}

	public void setReadButNotStored(String readButNotStored) {
		this.readButNotStored = readButNotStored;
	}

	public int getNotStored() {
		return notStored;
	}

	public void setNotStored(int notStored) {
		this.notStored = notStored;
	}

	public boolean isStored() {
		return stored;
	}

	public void setStored(boolean stored) {
		this.stored = stored;
	}

	// auto-generated, if not set (see ObjectId)
	@Id
	private ObjectId id;

	// value types are automatically persisted
	private String firstName, lastName;

	// only non-null values are stored
	private Long salary = null;

	// by default fields are @Embedded
	// Address address;

	// references can be saved without automatic loading
	private Key<Employee> manager;

	// refs are stored**, and loaded automatically
	@Reference
	private List<Employee> underlings = new ArrayList<Employee>();

	// fields can be renamed
	@Property("started")
	private Date startDate;

	@Property("left")
	private Date endDate;

	// fields can be indexed for better performance
	@Indexed
	private boolean active = false;

	// fields can loaded, but not saved
	@NotSaved
	private String readButNotStored;

	// fields can be ignored (no load/save)
	@Transient
	private int notStored;

	// not @Transient, will be ignored by Serialization/GWT for example.
	private transient boolean stored = true;

	// Lifecycle methods -- Pre/PostLoad, Pre/PostPersist...
	@PostLoad
	void postLoad(DBObject dbObj) {
		System.out.println(getClass().getSimpleName() + "#postLoad()");
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String toString() {
		return ObjectUtils.dumpViaReflection(this);
	}
}
