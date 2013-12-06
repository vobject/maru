package maru.map.views.gl;

import maru.map.jobs.gl.GLProjectAnimationJob;
import maru.map.jobs.gl.GLProjectDrawJob;

public interface IGLDrawJobRunner
{
    void addProjectDrawJob(GLProjectDrawJob job);
    void removeProjectDrawJob(GLProjectDrawJob job);

    void addProjectAnimationJob(GLProjectAnimationJob job);
    void removeProjectAnimationJob(GLProjectAnimationJob job);

    void redraw();
}
