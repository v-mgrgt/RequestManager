package backend.window.main.bar.listener;

import frontend.controlElement.Label;
import frontend.window.optionDialog.MessageDialog;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.util.List;
import java.util.Map;

public record Notification<K>(int numberOfOperations) {

    private @NotNull PlainDocument createModel(@NotNull Map<K, List<String>> notificationMap) {
        PlainDocument plainDocument = new PlainDocument();
        try {
            for (Map.Entry<K, List<String>> entry : notificationMap.entrySet()) {
                for (String value : entry.getValue()) {
                    try {
                        plainDocument.insertString(plainDocument.getLength(),
                                entry.getKey() + ": " + value + System.lineSeparator(), null);
                    } catch (BadLocationException ignored) {}
                }
            }
            plainDocument.replace(plainDocument.getLength() - System.lineSeparator().length(),
                    System.lineSeparator().length(), null, null);
        } catch (BadLocationException ignored) {}

        return plainDocument;
    }

    public void showNotificationDisplay(int notifications) {
        if (notifications == numberOfOperations) {
            new MessageDialog.Error("Ошибка при выполнении операции, повторите попытку.");
        } else if (notifications == 0) {
            new MessageDialog.Info("Операция успешно завершена!");
        } else {
            new MessageDialog.Warning("Не удалось завершить операцию! Завершено "
                    + (numberOfOperations - notifications) + " из " + numberOfOperations + ".");
        }
    }

    public void showNotificationDisplay(@NotNull Map<K, List<String>> notificationMap) {
        int[] notifications = {0};
        notificationMap.forEach((k, v) -> {
            if (!v.isEmpty()) notifications[0] += 1;
        });

        if (notifications[0] == numberOfOperations) {
            new MessageDialog.Error("Ошибка при выполнении операции, повторите попытку.");
        } else if (notifications[0] == 0) {
            new MessageDialog.Info("Операция успешно завершена!");
        } else {
            int maxRow = 10, maxListSize = 0;
            for (Map.Entry<K, List<String>> entry : notificationMap.entrySet()) {
                int size = entry.getValue().size();
                if (maxListSize < size) {
                    maxListSize = size;
                    if (maxListSize >= maxRow) break;
                }
            }

            JTextArea textArea = new JTextArea(createModel(notificationMap), null, Math.min(maxListSize, maxRow), 0);
            textArea.setEditable(false);
            textArea.setMargin(new Insets(0, 4, 0, 4));

            new MessageDialog.Warning(new JComponent[]{
                    new Label("<html><body>Не удалось завершить операцию! Завершено "
                            + (numberOfOperations - notifications[0]) + " из " + numberOfOperations
                            + ".<br>Необработанные элементы:</body></html>"),
                    new JScrollPane(textArea)});
        }
    }
}
