package models.data;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.PersistenceException;
import javax.persistence.Table;

import com.avaje.ebean.Ebean;

import models.data.manager.DataElement;

/**
 * Represents a Grade or Age category of a Indepent/User.
 * @author Felix Van der Jeugt
 */
@Entity @Table(name="Grades")
public class Grade implements DataElement {

    /**
     * name
     */
    @Id public String name;

    /**
     * lower age bound
     */
    public int lowerbound;
    /**
     * upper age bound
     */
    public int upperbound;

    /**
     * Creates a new Grade based on the provided name, loweround and upperbound.
     * The lower and upper bounds are the ages in years. The upperbound age is
     * not included.
     * @param name The name of the new Grade.
     * @param lowerbound The lowest age for students in this grade.
     * @param upperbound The age students are no longer in this grade.
     */
    public Grade(String name, int lowerbound, int upperbound) {
        this.name = name;
        this.lowerbound = lowerbound;
        this.upperbound = upperbound;
    }

    /**
     * Returns the name of the grade.
     * @return The name of the grade.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the lower bound of the grade.
     * @return The lowest age for students in this grade.
     */
    public int getLowerBound() {
        return lowerbound;
    }

    /**
     * Returns the upper mound of the grade. Students of this age "graduate".
     * @return The graduating age for this category.
     */
    public int getUpperBound() {
        return upperbound;
    }

    @Override public String[] strings() {
        return new String[]{
            name,
            Integer.toString(lowerbound),
            Integer.toString(upperbound)
        };
    }

    @Override public String id() {
        return name;
    }

    /**
     *
     * @return all the Grades in the system
     */
    public static List<Grade> getGrades(){
        try{
            return Ebean.find(Grade.class).findList();
        }catch(PersistenceException pe){
            return new ArrayList<Grade>();
        }
    }

}
