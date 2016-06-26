package com.github.jenkins.lastchanges;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.file.Paths;

import static com.github.jenkins.lastchanges.LastChanges.lastChanges;
import static com.github.jenkins.lastchanges.LastChanges.repository;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.contentOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by rmpestano on 6/5/16.
 */

@RunWith(JUnit4.class)
public class LastChangesTest {
    private static final String newLine = System.getProperty("line.separator");

    File diffFile = new File("target/diff.txt");
    String gitRepoPath;

    @Before
    public void before() {
        if (diffFile.exists()) {
            diffFile.delete();
        }

        gitRepoPath = LastChangesTest.class.getResource("/git-sample-repo").getPath();
    }

    @Test
    public void shouldInitLastChanges() {
        assertNotNull(repository(gitRepoPath));
    }

    @Test
    public void shouldNotInitLastChangesWithBlankPath() {
        try {
            repository("");
        } catch (RuntimeException e) {
            assertEquals("Path cannot be empty.", e.getMessage());
        }
    }

    @Test
    public void shouldNotInitLastChangesWithNonExistingRepository() {
        String repoPath = Paths.get("").toAbsolutePath().toString();
        try {
            repository(repoPath);
        } catch (RuntimeException e) {
            assertEquals(String.format("No git repository found at %s.", repoPath), e.getMessage());
        }
    }


    @Test
    public void shouldWriteLastChanges() throws FileNotFoundException {
        lastChanges(repository(gitRepoPath), new FileOutputStream(diffFile));
        assertThat(diffFile).exists().isFile();
        assertThat(contentOf(diffFile)).isEqualToIgnoringWhitespace("Commit: bb8a132b314888f2e8bee83bf534fa3e3f2815f9"+newLine +
                "Author: Martin Vysny"+newLine +
                "E-mail: mvy@whitestein.com"+newLine +
                "Date: May 30, 2016 10:49:58 AM GMT+02:00"+newLine +
                "Message: Added javadoc"+newLine +
                ""+newLine +
                ""+newLine +
                "diff --git a/kotlinee-framework/src/main/java/com/github/kotlinee/framework/vaadin/VaadinUtils.kt b/kotlinee-framework/src/main/java/com/github/kotlinee/framework/vaadin/VaadinUtils.kt"+newLine +
                "index 6d28c9b..bcc2ac0 100644"+newLine +
                "--- a/kotlinee-framework/src/main/java/com/github/kotlinee/framework/vaadin/VaadinUtils.kt"+newLine +
                "+++ b/kotlinee-framework/src/main/java/com/github/kotlinee/framework/vaadin/VaadinUtils.kt"+newLine +
                "@@ -31,6 +31,12 @@"+newLine +
                " /**"+newLine +
                "  * Creates a container which lists all instances of given entity. To restrict the list to a particular entity only,"+newLine +
                "  * simply call [JPAContainer.addContainerFilter] on the container produced."+newLine +
                "+ *"+newLine +
                "+ * Containers produced by this method have the following properties:"+newLine +
                "+ * * The container's [Item] IDs are not the entity instances themselves - instead, [Item] ID contains the value of the JPA entity ID. This is important when using the container"+newLine +
                "+ * together with [AbstractSelect] as the select's value is taken amongst the Item ID."+newLine +
                "+ * * [Item]'s Property IDs are [String] values - the field names of given JPA bean."+newLine +
                "+ *"+newLine +
                "  * @param entity the entity type"+newLine +
                "  * @return the new container which can be assigned to a [Grid]"+newLine +
                "  */"+newLine +
                "@@ -279,9 +285,12 @@"+newLine +
                "  * An utility method which adds an item and sets item's caption."+newLine +
                "  * @param the Identification of the item to be created."+newLine +
                "  * @param caption the new caption"+newLine +
                "+ * @return the newly created item ID."+newLine +
                "  */"+newLine +
                " fun AbstractSelect.addItem(itemId: Any?, caption: String) = addItem(itemId).apply { setItemCaption(itemId, caption) }!!"+newLine +
                " "+newLine +
                "+"+newLine +
                "+"+newLine +
                " /**"+newLine +
                "  * Walks over this component and all descendants of this component, breadth-first."+newLine +
                "  * @return iterable which iteratively walks over this component and all of its descendants.");

    }

}