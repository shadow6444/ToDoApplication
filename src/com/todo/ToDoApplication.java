package com.todo;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.border.EmptyBorder;

class Task {
	String title;
	String dueDate;
	boolean isCompleted;

	Task(String title, String dueDate) {
		this.title = title;
		this.dueDate = dueDate;
		this.isCompleted = false;
	}

	public String toString() {
		return (isCompleted ? "[Done] " : "[Pending] ") + title + " (Due: " + dueDate + ")";
	}
}

public class ToDoApplication {

	private JFrame frame;
	private JTextField titleField;
	private JTextField dueDateField;
	private DefaultListModel<Task> taskListModel;
	private JList<Task> taskJList;
	private java.util.ArrayList<Task> taskArray;

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				ToDoApplication window = new ToDoApplication();
				window.frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public ToDoApplication() {
		initialize();
	}

	private void initialize() {
		frame = new JFrame("To-Do List Application");
		frame.setBounds(100, 100, 600, 550);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout());

		taskArray = new java.util.ArrayList<>();
		taskListModel = new DefaultListModel<>();
		taskJList = new JList<>(taskListModel);
		taskJList.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		taskJList.setBackground(new Color(245, 245, 245));
		taskJList.setForeground(Color.BLACK);
		taskJList.setSelectionBackground(new Color(173, 216, 230));
		taskJList.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "Task List", 0,
				0, new Font("Segoe UI", Font.BOLD, 16)));

		JPanel inputPanel = new JPanel();
		inputPanel.setLayout(new GridLayout(3, 2, 10, 5));
		inputPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		inputPanel.setBackground(new Color(230, 240, 255));

		JLabel titleLabel = new JLabel("Task Title:");
		titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
		inputPanel.add(titleLabel);

		titleField = new JTextField();
		titleField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		inputPanel.add(titleField);

		JLabel dateLabel = new JLabel("Due Date (YYYY-MM-DD):");
		dateLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
		inputPanel.add(dateLabel);

		dueDateField = new JTextField();
		dueDateField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		inputPanel.add(dueDateField);

		JButton addButton = new JButton("Add Task");
		styleButton(addButton);
		inputPanel.add(addButton);

		JButton sortButton = new JButton("Sort by Due Date");
		sortButton.setForeground(new Color(0, 128, 192));
		styleButton(sortButton);
		inputPanel.add(sortButton);

		frame.getContentPane().add(inputPanel, BorderLayout.NORTH);

		JScrollPane scrollPane = new JScrollPane(taskJList);
		scrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setBackground(new Color(240, 248, 255));
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

		JButton toggleDoneButton = new JButton("Toggle Done");
		toggleDoneButton.setForeground(new Color(0, 128, 192));
		styleButton(toggleDoneButton);
		buttonPanel.add(toggleDoneButton);

		JButton deleteButton = new JButton("Delete Task");
		deleteButton.setForeground(new Color(0, 128, 192));
		styleButton(deleteButton);
		buttonPanel.add(deleteButton);

		JButton showPendingButton = new JButton("Show Pending");
		showPendingButton.setForeground(new Color(0, 128, 192));
		styleButton(showPendingButton);
		buttonPanel.add(showPendingButton);

		JButton showCompletedButton = new JButton("Show Completed");
		showCompletedButton.setForeground(new Color(0, 128, 192));
		styleButton(showCompletedButton);
		buttonPanel.add(showCompletedButton);

		JButton showAllButton = new JButton("Show All");
		showAllButton.setForeground(new Color(0, 128, 192));
		styleButton(showAllButton);
		buttonPanel.add(showAllButton);

		JButton exitButton = new JButton("Exit App");
		exitButton.setForeground(new Color(0, 128, 192));
		styleButton(exitButton);
		buttonPanel.add(exitButton);

		frame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
	}

	private void styleButton(JButton button) {
		button.setBackground(new Color(70, 130, 180));
		button.setForeground(new Color(0, 128, 192));
		button.setFocusPainted(false);
		button.setFont(new Font("Segoe UI", Font.BOLD, 16));
		button.setPreferredSize(new Dimension(165, 35));
	}
}
