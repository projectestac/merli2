package cat.xtec.merli.client.zonaclic.convert;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import cat.xtec.merli.Application;
import cat.xtec.merli.client.LomConverter;
import cat.xtec.merli.client.zonaclic.domain.Level;
import cat.xtec.merli.client.zonaclic.domain.Locale;
import cat.xtec.merli.client.zonaclic.domain.Project;
import cat.xtec.merli.domain.UID;
import cat.xtec.merli.domain.taxa.*;
import cat.xtec.merli.domain.type.*;
import cat.xtec.merli.domain.voc.*;
import cat.xtec.merli.domain.lom.*;


/**
 * Converts Zona Clic projects into LOM resources.
 */
public class ProjectConverter implements LomConverter<Project> {

    /**
     * Transforms a project into a new LOM resource instance.
     *
     * @param project           Project containing the data
     * @return                  New resource object
     */
    public Resource convert(Project project) {
        Resource resource = new Resource();

        populateGeneral(resource, project);
        populateEducational(resource, project);
        populateLifeCycle(resource, project);
        populateRights(resource, project);
        populateMetadata(resource, project);
        populateTechnical(resource, project);
        populateClasses(resource, project);

        return resource;
    }


    /**
     * Populates the general details of a resource given a project.
     *
     * @param resource          Resource to populate
     * @param project           Project containing the data
     */
    private void populateGeneral(Resource resource, Project project) {
        GeneralDetails general = resource.getGeneralDetails();

        general.getTitles().add(getTitle(project));
        general.getIdentifiers().add(getIdentifier(project));
        general.getDescriptions().addAll(project.getDescriptions());
        general.getLanguages().addAll(getLanguages(project));
    }


    /**
     * Populates the educational details of a resource given a project.
     *
     * @param resource          Resource to populate
     * @param project           Project containing the data
     */
    private void populateEducational(Resource resource, Project project) {
        EducationalDetails educational = resource.getEducationalDetails();

        educational.getContexts().addAll(getContexts(project));
        educational.getResourceTypes().add(ResourceType.TOOL);
        educational.getUserRoles().add(UserRole.LEARNER);
        educational.getUserRoles().add(UserRole.TEACHER);
    }


    /**
     * Populates the life-cycle details of a resource given a project.
     *
     * @param resource          Resource to populate
     * @param project           Project containing the data
     */
    private void populateLifeCycle(Resource resource, Project project) {
        LifeCycleDetails lifecycle = resource.getLifeCycleDetails();
        List<Contribution> contributions = lifecycle.getContributions();

        lifecycle.setStatus(Status.REVISED);
        contributions.add(getAuthorship(project));
    }


    /**
     * Populates the rights details of a resource given a project.
     *
     * @param resource          Resource to populate
     * @param project           Project containing the data
     */
    private void populateRights(Resource resource, Project project) {
        RightsDetails rights = resource.getRightsDetails();

        rights.getDescriptions().addAll(project.getLicenses());
    }


    /**
     * Populates the metadata details of a resource given a project.
     *
     * @param resource          Resource to populate
     * @param project           Project containing the data
     */
    private void populateMetadata(Resource resource, Project project) {
        MetadataDetails metadata = resource.getMetadataDetails();
        List<Contribution> contributions = metadata.getContributions();

        contributions.add(getMetadataCreator(project));
        contributions.add(getMetadataValidator(project));
    }


    /**
     * Populates the technical details of a resource given a project.
     *
     * @param resource          Resource to populate
     * @param project           Project containing the data
     */
    private void populateTechnical(Resource resource, Project project) {
        TechnicalDetails technical = resource.getTechnicalDetails();

        technical.setLocation(project.getLocation());
        technical.getFormats().add(Format.APPLICATION_BINARY);
    }


    /**
     * Populates the classifications of a resource given a project.
     *
     * @param resource          Resource to populate
     * @param project           Project containing the data
     */
    private void populateClasses(Resource resource, Project project) {
        List<Classification> classes = resource.getClasses();
        classes.add(getCategoryClasses(project));
    }


    /**
     * Extracts the resource title from a project.
     */
    private LangString getTitle(Project project) {
        return new LangString(project.getTitle(), Language.CATALAN);
    }


    /**
     * Extracts a resource identifier from a project. An identifier is
     * composed of a catalog and an entry within that catalog.
     */
    private Identifier getIdentifier(Project project) {
        return new Identifier(project.getId());
    }


    /**
     * Extracts the resource author contribution from a project. That is,
     * who created the content of the resource and when.
     */
    private Contribution getAuthorship(Project project) {
        Contribution authorship = new Contribution();

        authorship.setRole(ContributorRole.AUTHOR);
        authorship.setEntity(project.getAuthor());
        authorship.setTimePoint(project.getDate());

        return authorship;
    }


    /**
     * Returns the resource metadata creator contribution. That is,
     * who created the metadata of the resource and when.
     */
    private Contribution getMetadataCreator(Project project) {
        Contribution creator = new Contribution();

        creator.setRole(ContributorRole.CREATOR);
        creator.setEntity(Application.NAME);
        creator.setTimePoint(new Date());

        return creator;
    }


    /**
     * Returns the resource metadata validator contribution. That is,
     * who validated the integrity of the metadata and when.
     */
    private Contribution getMetadataValidator(Project project) {
        Contribution validator = new Contribution();

        validator.setRole(ContributorRole.VALIDATOR);
        validator.setEntity(Application.NAME);
        validator.setTimePoint(new Date());

        return validator;
    }


    /**
     * Returns a classification section descriving the project with
     * categories from the built-in curriculum taxonomy.
     */
    private Classification getCategoryClasses(Project project) {
        Classification classes = new Classification();
        List<Entity> entities = classes.getEntities();

        classes.setPurpose(Purpose.EDUCATIONAL_CONTEXT);
        entities.addAll(getCategories(project));

        return classes;
    }


    /**
     * Extracts the categories where the resource belongs from a project.
     * It is assumed that the category identifiers exist on the built-in
     * curriculum taxonomy.
     */
    private List<Entity> getCategories(Project project) {
        List<Entity> categories = new ArrayList<>();

        for (DefaultCategory c : DefaultCategory.values()) {
            categories.add(c.category());
        }

        return categories;
    }


    /**
     * Extracts the thesaurus descriptors from a project. It is assumed
     * that the keyword identifiers exist on the built-in thesaurus.
     */
    private List<Entity> getTerms(Project project) {
        List<Entity> terms = new ArrayList<>();

        for (Integer id : project.getKeywords()) {
            if (id instanceof Integer) {
                terms.add(new Term(UID.valueOf(id)));
            }
        }

        return terms;
    }


    /**
     * Extracts the resource educational contexts from a project.
     */
    private List<Context> getContexts(Project project) {
        List<Context> contexts = new ArrayList<>();

        for (Level level : project.getLevels()) {
            if (level instanceof Level) {
                contexts.add(level.context());
            }
        }

        return contexts;
    }


    /**
     * Extracts the resource user languages from a project.
     */
    private List<Language> getLanguages(Project project) {
        List<Language> languages = new ArrayList<>();

        for (Locale locale : project.getLocales()) {
            if (locale instanceof Locale) {
                languages.add(locale.language());
            }
        }

        return languages;
    }

}
