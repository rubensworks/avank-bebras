package test;

import java.util.List;
import java.util.ArrayList;

import org.junit.Test;
import static org.fest.assertions.Assertions.assertThat;

import play.mvc.Content;
import static play.test.Helpers.contentType;
import static play.test.Helpers.contentAsString;

import models.data.Link;
import views.html.index;

import test.ContextTest;

/**
 * An example test running in the application's context.
 * @author Felix Van der Jeugt
 * @author Sander Demeester
 */
public class ExampleTest extends ContextTest {

    @Test
    public void testTemplate() {
        // Index is the name of our scala template.
        // The scala source file takes one string argument thet the
        // template will render
        List<Link> links = new ArrayList<Link>();
        links.add(new Link("Bebras", "http://www.bebras.be"));
        Content htmlContent = index.render("Test-string", links);

        // Test the content Type
        assertThat(contentType(htmlContent)).isEqualTo("text/html");
        // Test that the html Content contains a string
        assertThat(contentAsString(htmlContent)).contains(
            "Test-string");
    }

}
