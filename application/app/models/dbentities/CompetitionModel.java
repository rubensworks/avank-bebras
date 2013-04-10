package models.dbentities;

import models.competition.CompetitionType;
import models.management.ManageableModel;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.util.Date;

/**
 * Database entity for competition.
 *
 * @author Kevin Stobbelaar.
 *
 */
@Entity
@Table(name="contests")
public class CompetitionModel extends ManageableModel {

    private static final long serialVersionUID = 1L;

    @Constraints.Required
    public String creator;

    @Id
    public String id;

    @Constraints.Required
    public String name;

    @Constraints.Required
    @Enumerated(EnumType.STRING)
    public CompetitionType type;

    @Constraints.Required
    public boolean active;

    @Constraints.Required
    public Date starttime;

    @Constraints.Required
    public Date endtime;

    /**
     * Returns those values that have to be represented in a table.
     *
     * @return array with the current values of the fields to be represented in the table
     */
    @Override
    public String[] getFieldValues() {
        String[] fieldValues = {name, type.name(), Boolean.toString(active), starttime.toString(), endtime.toString()};
        return fieldValues;
    }

    /**
     * Returns the id of the object.
     *
     * @return id
     */
    @Override
    public String getID() {
        return id;
    }

}
