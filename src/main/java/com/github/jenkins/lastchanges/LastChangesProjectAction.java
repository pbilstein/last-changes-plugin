package com.github.jenkins.lastchanges;

import hudson.model.AbstractProject;
import hudson.model.Job;
import hudson.model.ProminentProjectAction;
import hudson.model.Run;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LastChangesProjectAction extends LastChangesBaseAction implements ProminentProjectAction {

    private final AbstractProject<?, ?> project;

    private String jobName;

    public LastChangesProjectAction(AbstractProject<?, ?> project) {
        this.project = project;
    }

    public String job() {
        if (jobName == null) {
            jobName = project.getName();
        }
        return jobName;
    }

    public Job<?, ?> getProject() {
        return project;
    }

    @Override
    protected File dir() {
        Run<?, ?> run = this.project.getLastCompletedBuild();
        if (run != null) {
            File archiveDir = getBuildArchiveDir(run);

            if (archiveDir.exists()) {
                return archiveDir;
            }
        }

        return getProjectArchiveDir();
    }

    private File getProjectArchiveDir() {
        return new File(project.getRootDir(), LastChangesBaseAction.BASE_URL);
    }

    /**
     * Gets the directory where the HTML report is stored for the given build.
     */
    private File getBuildArchiveDir(Run<?, ?> run) {
        return new File(run.getRootDir(), LastChangesBaseAction.BASE_URL);
    }

    @Override
    protected String getTitle() {
        return this.project.getDisplayName();
    }

    public List<Run<?, ?>> getLastChangesBuilds() {
        List<Run<?, ?>> builds = new ArrayList<>();

        for (Run<?, ?> build : project.getBuilds()) {
            LastChangesBuildAction action = build.getAction(LastChangesBuildAction.class);
            if (action != null) {
                builds.add(build);
            }
        }

        return builds;
    }

}