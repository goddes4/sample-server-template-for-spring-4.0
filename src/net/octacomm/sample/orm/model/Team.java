package net.octacomm.sample.orm.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "teams")
@Setter
@Getter
@ToString
public class Team {

	@Id
	private int id;
	private String name;
	private int rating;
}
