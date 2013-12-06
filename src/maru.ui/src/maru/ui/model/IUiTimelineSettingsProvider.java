package maru.ui.model;

public interface IUiTimelineSettingsProvider
{
    void addTimelineSettingsListener(IUiTimelineSettingsListener listener);
    void removeTimelineSettingsListener(IUiTimelineSettingsListener listener);
}
