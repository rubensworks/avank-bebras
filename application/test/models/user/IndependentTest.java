package models.user;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.persistence.PersistenceException;

import junit.framework.Assert;

import models.dbentities.ClassGroup;
import models.dbentities.ClassPupil;
import models.dbentities.UserModel;

import org.junit.Before;
import org.junit.Test;

import com.avaje.ebean.Ebean;

import test.ContextTest;
import static models.user.UserTests.createTestUserModel;

public class IndependentTest extends ContextTest {

    @Before
    public void clear(){
        List<UserModel> um = Ebean.find(UserModel.class).findList();
        for(UserModel u : um) u.delete();

        List<ClassGroup> cg = Ebean.find(ClassGroup.class).findList();
        for(ClassGroup c : cg) c.delete();

        List<ClassPupil> cp = Ebean.find(ClassPupil.class).findList();
        for(ClassPupil c:cp)c.delete();
    }

    @Test
    public void testGetCurrentClass() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, 1);
        Date exp = c.getTime();
        UserModel data = createTestUserModel(UserType.PUPIL_OR_INDEP);
        ClassGroup cl1 = new ClassGroup();
        cl1.id = 1;
        cl1.expdate=exp;
        ClassGroup cl2 = new ClassGroup();
        cl2.id = 2;
        cl2.expdate=exp;
        ClassGroup cl3 = new ClassGroup();
        cl3.id = 3;
        cl3.expdate=exp;
        data.classgroup = cl1.id;

        ClassPupil cp1 = new ClassPupil();
        cp1.classid=cl2.id;
        cp1.indid=data.id;
        ClassPupil cp2 = new ClassPupil();
        cp2.classid=cl3.id;
        cp2.indid=data.id;

        try{
            data.save();
            cl1.save();
            cl2.save();
            cl3.save();
            cp1.save();
            cp2.save();
        }catch(PersistenceException p){
            Assert.fail("Something went wrong with the saving of the independent");
        }

        UserModel find = Ebean.find(UserModel.class).where().eq("id", data.id).findUnique();
        Independent iff = new Independent(find);

        ClassGroup fff = iff.getCurrentClass();
        Assert.assertNotNull(fff);
        Assert.assertEquals(cl1.id, fff.id);
    }

    @Test
    public void testGetPreviousClasses() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, 1);
        Date exp = c.getTime();

        UserModel data = createTestUserModel(UserType.PUPIL_OR_INDEP);
        ClassGroup cl1 = new ClassGroup();
        cl1.id = 1;
        cl1.expdate=exp;
        ClassGroup cl2 = new ClassGroup();
        cl2.id = 2;
        cl2.expdate=exp;
        ClassGroup cl3 = new ClassGroup();
        cl3.id = 3;
        cl3.expdate=exp;
        data.classgroup = cl1.id;

        ClassPupil cp1 = new ClassPupil();
        cp1.classid=cl2.id;
        cp1.indid=data.id;
        ClassPupil cp2 = new ClassPupil();
        cp2.classid=cl3.id;
        cp2.indid=data.id;

        try{
            data.save();
            cl1.save();
            cl2.save();
            cl3.save();
            cp1.save();
            cp2.save();
        }catch(PersistenceException p){
            p.printStackTrace();
            Assert.fail("Something went wrong with the saving of the independent");
        }

        UserModel find = Ebean.find(UserModel.class).where().eq("id", data.id).findUnique();
        Independent iff = new Independent(find);

        Collection<ClassGroup> fff = iff.getPreviousClasses();
        Assert.assertEquals(2, fff.size());
        HashSet<Integer> ids = new HashSet<Integer>();
        for(ClassGroup cg : fff){
            ids.add(cg.id);
        }
        Assert.assertTrue(ids.contains(cl2.id));
        Assert.assertTrue(ids.contains(cl3.id));
    }

    @Test
    public void isActiveInClass(){
        Independent i = new Independent(
                new UserModel(
                        "a",
                        UserType.PUPIL_OR_INDEP,
                        "hh",
                        new Date(17),
                        new Date(17),
                        "gg",
                        "hh",
                        "tt",
                        Gender.Other,
                        "hh"
                        ));
        int actualClassID = 42;
        i.data.classgroup = actualClassID;

        Assert.assertTrue("True case failed", i.isActiveInClass(actualClassID));
        Assert.assertFalse("False case failed", i.isActiveInClass(actualClassID+1));

        i.data.classgroup = null;
        Assert.assertFalse("Null case failed",i.isActiveInClass(actualClassID));
    }

}
