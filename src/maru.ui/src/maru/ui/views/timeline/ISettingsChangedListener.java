package maru.ui.views.timeline;

interface ISettingsChangedListener
{
    void stepSizeChanged(long stepSize);
    void playbackSpeedChanged(double multiplicator);
    void realtimeModeChanged(boolean realtime);
}
