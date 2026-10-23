package ui;

import model.LockSystem;
import service.AdminService;
import service.FingerprintService;
import service.PinService;
import util.DataManager;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private LockSystem system = new LockSystem();

    public MainFrame() {

        setTitle("Биометрический замок");
        setSize(800, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        DataManager.loadData(system);

        // Заголовок
        JLabel titleLabel = new JLabel("БИОМЕТРИЧЕСКИЙ ЗАМОК", SwingConstants.CENTER);

        titleLabel.setFont(
                new Font("Arial", Font.BOLD, 24));

        add(titleLabel, BorderLayout.NORTH);


        // Центральная панель
        JPanel centerPanel = new JPanel();

        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        centerPanel.setLayout(new GridLayout(7, 1, 10, 10));


        // Дисплей
        JTextArea display = new JTextArea("Замок закрыт");

        display.setEditable(false);

        display.setFont(new Font("Consolas", Font.BOLD, 18));
        display.setBackground(Color.BLACK);
        display.setForeground(Color.GREEN);

        JScrollPane displayScroll = new JScrollPane(display);


        // Состояние замка
        JLabel lockState = new JLabel("СОСТОЯНИЕ: ЗАКРЫТ", SwingConstants.CENTER);

        lockState.setFont(new Font("Arial", Font.BOLD, 18));


        // PIN
        JLabel pinLabel = new JLabel("PIN:");
        JTextField pinField = new JTextField(10);

        pinField.setEditable(false);

        JButton pinButton = new JButton("Открыть по PIN");

        JPanel pinPanel = new JPanel();

        pinPanel.add(pinLabel);
        pinPanel.add(pinField);
        pinPanel.add(pinButton);


        // Клавиатура
        JPanel keypadPanel = new JPanel();

        keypadPanel.setLayout(new GridLayout(4, 3, 5, 5));

        for (int i = 1; i <= 9; i++) {
            JButton button = new JButton(String.valueOf(i));
            button.setFont(new Font("Arial", Font.BOLD, 22));

            button.addActionListener(e -> {
                if (pinField.getText().length() < 4) {
                    pinField.setText(pinField.getText() + button.getText());
                }
            });
            keypadPanel.add(button);
        }

        JButton clearButton = new JButton("C");

        clearButton.setFont(new Font("Arial", Font.BOLD, 22));
        clearButton.addActionListener(e -> pinField.setText(""));

        JButton zeroButton = new JButton("0");

        zeroButton.setFont(new Font("Arial", Font.BOLD, 22));

        zeroButton.addActionListener(e -> {
            if (pinField.getText().length() < 4) {
                pinField.setText(pinField.getText() + "0");
            }
        });

        JButton backButton = new JButton("←");

        backButton.setFont(new Font("Arial", Font.BOLD, 22));

        backButton.addActionListener(e -> {
            String text = pinField.getText();
            if (!text.isEmpty()) {
                pinField.setText(text.substring(0, text.length() - 1));
            }
        });

        keypadPanel.add(clearButton);
        keypadPanel.add(zeroButton);
        keypadPanel.add(backButton);


        // Отпечаток
        JLabel fingerLabel = new JLabel("Отпечаток:");
        JTextField fingerField = new JTextField(10);
        JButton fingerButton = new JButton("Сканировать");

        JPanel fingerPanel = new JPanel();

        fingerPanel.add(fingerLabel);
        fingerPanel.add(fingerField);
        fingerPanel.add(fingerButton);


        // Кнопки действий
        JButton bellButton = new JButton("Вызов");
        JButton addFingerButton = new JButton("Добавить отпечаток");
        JButton changePinButton = new JButton("Сменить код доступа");
        JButton changeControlButton = new JButton("Сменить код контроля");

        JPanel actionPanel = new JPanel();

        actionPanel.add(bellButton);
        actionPanel.add(addFingerButton);
        actionPanel.add(changePinButton);
        actionPanel.add(changeControlButton);


        // Журнал
        JTextArea logArea = new JTextArea();

        logArea.setEditable(false);

        JScrollPane logScroll = new JScrollPane(logArea);

        logScroll.setBorder(BorderFactory.createTitledBorder("Журнал событий"));


        // Добавление элементов
        centerPanel.add(displayScroll);
        centerPanel.add(lockState);
        centerPanel.add(pinPanel);
        centerPanel.add(keypadPanel);
        centerPanel.add(fingerPanel);
        centerPanel.add(actionPanel);
        centerPanel.add(logScroll);

        add(centerPanel, BorderLayout.CENTER);


        // Вход по PIN
        pinButton.addActionListener(e -> {
            if (PinService.checkPin(pinField.getText(), system)) {
                system.unlock();
                display.setForeground(Color.GREEN);
                display.setText("ДОСТУП РАЗРЕШЕН");
                lockState.setText("СОСТОЯНИЕ: ОТКРЫТ");
                logArea.append("Успешный вход по PIN\n");

            } else {
                display.setForeground(Color.RED);
                display.setText("НЕВЕРНЫЙ PIN");
                logArea.append("Ошибка PIN\n");
            }
        });


        // Вход по отпечатку
        fingerButton.addActionListener(e -> {
            if (FingerprintService.checkFingerprint(fingerField.getText(), system)) {
                system.unlock();
                display.setForeground(Color.GREEN);
                display.setText("ОТПЕЧАТОК ПРИНЯТ");
                lockState.setText("СОСТОЯНИЕ: ОТКРЫТ");
                logArea.append("Успешный вход по отпечатку\n");

            } else {
                display.setForeground(Color.RED);
                display.setText("ОТПЕЧАТОК НЕ НАЙДЕН");
                logArea.append("Неизвестный отпечаток\n");
            }
        });


        // Вызов
        bellButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "Звонок сработал");
            logArea.append("Нажата кнопка Вызов\n");
        });


        // Добавление отпечатка
        addFingerButton.addActionListener(e -> {
            String newFingerprint = JOptionPane.showInputDialog("Введите новый отпечаток");
            if (newFingerprint != null && !newFingerprint.isEmpty()) {
                AdminService.addFingerprint(newFingerprint, system);
                DataManager.saveData(system);
                JOptionPane.showMessageDialog(null, "Отпечаток добавлен");

                logArea.append("Добавлен отпечаток: " + newFingerprint + "\n");
            }
        });


        // Смена кода доступа
        changePinButton.addActionListener(e -> {
            if (system.isLocked()) {
                JOptionPane.showMessageDialog(null, "Сначала откройте дверь");
                return;
            }

            String controlCode = JOptionPane.showInputDialog("Введите код контроля");
            String newPin = JOptionPane.showInputDialog("Введите новый PIN");

            boolean result = AdminService.changeAccessCode(controlCode, newPin, system);

            if (result) {
                JOptionPane.showMessageDialog(null, "Код доступа изменён");
                DataManager.saveData(system);
                logArea.append("Изменён код доступа\n");

            } else {
                JOptionPane.showMessageDialog(null, "Неверный код контроля");
            }
        });


        // Смена кода контроля
        changeControlButton.addActionListener(e -> {
            if (system.isLocked()) {
                JOptionPane.showMessageDialog(null, "Сначала откройте дверь");
                return;
            }

            String oldControl = JOptionPane.showInputDialog("Введите текущий код контроля");
            String newControl = JOptionPane.showInputDialog("Введите новый код контроля");

            boolean result = AdminService.changeControlCode(oldControl, newControl, system);

            if (result) {
                JOptionPane.showMessageDialog(null, "Код контроля изменён");
                DataManager.saveData(system);
                logArea.append("Изменён код контроля\n");

            } else {
                JOptionPane.showMessageDialog(null, "Неверный код контроля");
            }
        });

        setVisible(true);
    }
}