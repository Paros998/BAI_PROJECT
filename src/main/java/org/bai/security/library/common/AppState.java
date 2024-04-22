package org.bai.security.library.common;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(access = AccessLevel.PROTECTED)
public class AppState {
    private AppMode appMode;

    protected AppState setAppMode(final AppMode appMode) {
        this.appMode = appMode;
        return this;
    }

    public enum AppMode {
        SAFE, UNSAFE
    }
}