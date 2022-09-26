package me.verwelius.ghjumper;

import org.kohsuke.github.GHRepository;
import org.kohsuke.github.PagedIterable;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

public class Gui {

    private final TrayIcon trayIcon;

    public Gui() {
        try {
            SystemTray tray = SystemTray.getSystemTray();
            Image image = Toolkit.getDefaultToolkit()
                    .createImage(getClass().getResource("/logo.png"));

            trayIcon = new TrayIcon(image, "GitHub Jumper");
            trayIcon.setImageAutoSize(true);
            trayIcon.setToolTip("GitHub Jumper");
            tray.add(trayIcon);
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
    }

    public void setMenu(String login, PagedIterable<GHRepository> ownRepos, PagedIterable<GHRepository> starredRepos) {
        PopupMenu popup = new PopupMenu();

        Menu userMenu = new Menu(login);

        MenuItem aboutPageItem = new MenuItem("Open about page");
        MenuItem reposPageItem = new MenuItem("Open repositories page");
        MenuItem starsPageItem = new MenuItem("Open starred page");

        aboutPageItem.addActionListener(e -> openInBrowser("https://github.com/" + login));
        reposPageItem.addActionListener(e -> openInBrowser("https://github.com/" + login + "?tab=repositories"));
        starsPageItem.addActionListener(e -> openInBrowser("https://github.com/" + login + "?tab=stars"));

        userMenu.add(aboutPageItem);
        userMenu.add(reposPageItem);
        userMenu.add(starsPageItem);

        Menu ownReposMenu = new Menu("Personal Repositories");
        ownRepos.forEach(repo -> {
            String repoName = repo.getName();
            MenuItem repoItem = new MenuItem(repoName);
            repoItem.addActionListener(e -> openInBrowser("https://github.com/" + repo.getFullName()));

            ownReposMenu.add(repoItem);
        });

        Menu starredReposMenu = new Menu("Starred Repositories");
        starredRepos.forEach(repo -> {
            String repoName = repo.getName();
            MenuItem repoItem = new MenuItem(repoName);
            repoItem.addActionListener(e -> openInBrowser("https://github.com/" + repo.getFullName()));

            starredReposMenu.add(repoItem);
        });

        MenuItem exitItem = new MenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));

        popup.add(userMenu);
        popup.addSeparator();
        popup.add(ownReposMenu);
        popup.add(starredReposMenu);
        popup.addSeparator();
        popup.add(exitItem);

        trayIcon.setPopupMenu(popup);
    }

    private void openInBrowser(String url) {
        try {
            Desktop.getDesktop().browse(new URL(url).toURI());
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

}
