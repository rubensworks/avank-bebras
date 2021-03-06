package models.competition;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.Page;
import controllers.competition.routes;
import models.dbentities.ClassGroup;
import models.dbentities.CompetitionModel;
import models.dbentities.ContestClass;
import models.management.Manager;
import models.management.ModelState;
import play.mvc.Call;

import java.util.ArrayList;
import java.util.List;

/**
 * Manager for contest classes.
 *
 * @author Kevin Stobbelaar.
 */
public class CompetitionClassManager extends Manager<ClassGroup> {

    private String contestid;
    private String teacherid;
    private ExpressionList<ClassGroup> expressionList;

    /**
     * Constructor for manager class.
     *
     * @param contestid  contest id
     * @param state      model state
     * @param orderBy    column to be ordered on
     */
    public CompetitionClassManager(ModelState state, String orderBy, String contestid, String teacherid) {
        super(ClassGroup.class, state, orderBy, "name");
        this.contestid = contestid;
        this.teacherid = teacherid;
        setPageSize(5);

        // setting the expression list for this manager
        CompetitionModel contest = Ebean.find(CompetitionModel.class).where().idEq(contestid).findUnique();
        List<ContestClass> contestClasses = Ebean.find(ContestClass.class).where().eq("contestid", contest).findList();
        //List<ClassGroup> classIds = new ArrayList<ClassGroup>();
        //for (ContestClass contestClass : contestClasses){
        //    classIds.add(Ebean.find(ClassGroup.class).where().eq("id",contestClass.classid.id).findUnique());
        //}
        List<Integer> classIds = new ArrayList<Integer>();
        for (ContestClass contestClass : contestClasses){
            classIds.add(contestClass.classid.id);
        }
        this.expressionList = getFinder()
                .where()
                .in("id", classIds)
                .eq("teacherid", teacherid);

        // disables the action buttons
        setHasActions(false);
    }

    /**
     * Returns a page with elements of type T.
     *
     * WARNING: it's better to override this method in your own manager!
     *
     * @param page     page number
     * @return the requested page
     */
    @Override
    public Page<ClassGroup> page(int page) {
        return expressionList
                .ilike(filterBy, "%" + filter + "%")
                .orderBy(orderBy + " " + order)
                .findPagingList(pageSize)
                .getPage(page);
    }

    /**
     * Returns the column headers for the objects of type T.
     *
     * @return column headers
     */
    @Override
    public List<String> getColumnHeaders() {
        ArrayList<String> columnHeaders = new ArrayList<String>();
        columnHeaders.add("id");
        columnHeaders.add("name");
        columnHeaders.add("schoolid");
        columnHeaders.add("teacherid");
        columnHeaders.add("level");
        columnHeaders.add("expdate");
        return columnHeaders;
    }

    /**
     * Returns the route that must be followed to refresh the list.
     *
     * @param page    current page number
     * @param orderBy current order by
     * @param order   current order
     * @param filter  filter on the items
     * @return Call Route that must be followed
     */
    @Override
    public Call getListRoute(int page, String orderBy, String order, String filter) {
        return routes.CompetitionClassController.list(contestid, page, orderBy, order, filter);
    }

    @Override
    public Call getAddRoute(){
        return routes.CompetitionClassController.register(contestid);
    }

    /**
     * Returns the prefix for translation messages.
     *
     * @return name
     */
    @Override
    public String getMessagesPrefix() {
        return "classes.main";
    }
}
