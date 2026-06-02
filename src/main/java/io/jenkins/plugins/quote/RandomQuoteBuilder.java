package io.jenkins.plugins.quote;

import hudson.Extension;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import hudson.tasks.Builder;
import hudson.tasks.BuildStepDescriptor;
import org.kohsuke.stapler.DataBoundConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class RandomQuoteBuilder extends Builder {

    private static final List<String> quotes = new ArrayList<>();
    private static final Random random = new Random();

    static {
        loadQuotes();
    }

    @DataBoundConstructor
    public RandomQuoteBuilder() {
    }

    @Override
    public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener) throws IOException, InterruptedException {
        if (!quotes.isEmpty()) {
            String quote = quotes.get(random.nextInt(quotes.size()));
            listener.getLogger().println("[Quote] " + quote);
        } else {
            listener.getLogger().println("[Quote] No quotes available.");
        }
        return true;
    }

    private static void loadQuotes() {
        InputStream is = RandomQuoteBuilder.class.getResourceAsStream("quotes.txt");
        if (is != null) {
            try (Scanner scanner = new Scanner(is, "UTF-8")) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine().trim();
                    if (!line.isEmpty()) {
                        quotes.add(line);
                    }
                }
            }
        }
    }

    @Extension
    public static final class DescriptorImpl extends BuildStepDescriptor<Builder> {
        @Override
        public String getDisplayName() {
            return "Print a random quote";
        }

        @Override
        public boolean isApplicable(Class<? extends AbstractBuild> jobType) {
            return true;
        }
    }
}