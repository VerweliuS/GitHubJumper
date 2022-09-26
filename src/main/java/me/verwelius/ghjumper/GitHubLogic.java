package me.verwelius.ghjumper;

import org.kohsuke.github.*;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class GitHubLogic {

    private final GitHub gitHub;
    private final Gui gui = new Gui();

    public GitHubLogic() {
        try {
            gitHub = new GitHubBuilder()
                    .withAppInstallationToken(System.getenv("GITHUB_TOKEN"))
                    .build();
            init();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void init() throws IOException {
        GHMyself me = gitHub.getMyself();
        String login = me.getLogin();

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                gui.setMenu(login, me.listRepositories(), me.listStarredRepositories());
            }
        },0, 10000);
    }

}
