package com.todo;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
	private ArrayList<Task> taskArray;

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

		taskArray = new ArrayList<>();
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
		addButton.addActionListener(e -> addTask());
		inputPanel.add(addButton);

		JButton sortButton = new JButton("Sort by Due Date");
		sortButton.setForeground(new Color(0, 128, 192));
		styleButton(sortButton);
		sortButton.addActionListener(e -> sortTasks());
		inputPanel.add(sortButton);

		frame.getContentPane().add(inputPanel, BorderLayout.NORTH);

		JScrollPane scrollPane = new JScrollPane(taskJList);
		scrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setBackground(new Color(240, 248, 255));
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

		String[] buttonNames = { "Toggle Done", "Delete Task", "Show Pending", "Show Completed", "Show All",
				"Exit App" };

		for (String name : buttonNames) {
			JButton btn = new JButton(name);
			btn.setForeground(new Color(0, 128, 192));
			styleButton(btn);

			if (name.equals("Toggle Done")) {
				btn.addActionListener(e -> toggleTaskDone());
			} else if (name.equals("Show Pending")) {
				btn.addActionListener(e -> showPendingTasks());
			} else if (name.equals("Show Completed")) {
				btn.addActionListener(e -> showCompletedTasks());
			} else if (name.equals("Show All")) {
				btn.addActionListener(e -> showAllTasks());
			} else if (name.equals("Delete Task")) {
				btn.addActionListener(e -> deleteTask());
			} else if (name.equals("Exit App")) {
				btn.addActionListener(e -> exitApp());
			}

			buttonPanel.add(btn);
		}

		frame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
	}

	private void styleButton(JButton button) {
		button.setBackground(new Color(70, 130, 180));
		button.setForeground(new Color(0, 128, 192));
		button.setFocusPainted(false);
		button.setFont(new Font("Segoe UI", Font.BOLD, 16));
		button.setPreferredSize(new Dimension(165, 35));
	}

	private void addTask() {
		String title = titleField.getText().trim();
		String dueDate = dueDateField.getText().trim();

		if (title.isEmpty()) {
			JOptionPane.showMessageDialog(frame, "Task title cannot be empty.");
			return;
		}

		if (!isValidDate(dueDate)) {
			JOptionPane.showMessageDialog(frame, "Invalid date format. Please use YYYY-MM-DD.");
			return;
		}

		if (isPastDate(dueDate)) {
			JOptionPane.showMessageDialog(frame, "Due date cannot be in the past.");
			return;
		}

		Task newTask = new Task(title, dueDate);
		taskArray.add(newTask);
		showAllTasks();
		titleField.setText("");
		dueDateField.setText("");
	}

	private void showAllTasks() {
		taskListModel.clear();
		for (Task task : taskArray) {
			taskListModel.addElement(task);
		}
	}

	private void showCompletedTasks() {
		taskListModel.clear();
		for (Task task : taskArray) {
			if (task.isCompleted) {
				taskListModel.addElement(task);
			}
		}
	}

	private void showPendingTasks() {
		taskListModel.clear();
		for (Task task : taskArray) {
			if (!task.isCompleted) {
				taskListModel.addElement(task);
			}
		}
	}

	private void toggleTaskDone() {
		Task selectedTask = taskJList.getSelectedValue();
		if (selectedTask != null) {
			selectedTask.isCompleted = !selectedTask.isCompleted;
			taskJList.repaint();
		}
	}

	private void deleteTask() {
		Task selectedTask = taskJList.getSelectedValue();
		if (selectedTask != null) {
			int confirm = JOptionPane.showConfirmDialog(frame, "Are you sure you want to delete this task?",
					"Delete Confirmation", JOptionPane.YES_NO_OPTION);
			if (confirm == JOptionPane.YES_OPTION) {
				taskArray.remove(selectedTask);
				taskListModel.removeElement(selectedTask);
			}
		}
	}

	private void sortTasks() {
		Collections.sort(taskArray, Comparator.comparing(t -> t.dueDate));
		showAllTasks();
	}

	private void exitApp() {
		int confirm = JOptionPane.showConfirmDialog(frame, "Are you sure you want to exit?", "Exit Confirmation",
				JOptionPane.YES_NO_OPTION);
		if (confirm == JOptionPane.YES_OPTION) {
			System.exit(0);
		}
	}

	private boolean isValidDate(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		sdf.setLenient(false);
		try {
			sdf.parse(date);
			return true;
		} catch (ParseException e) {
			return false;
		}
	}

	private boolean isPastDate(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		sdf.setLenient(false);
		try {
			Date inputDate = sdf.parse(date);
			Date today = sdf.parse(sdf.format(new Date()));
			return inputDate.before(today);
		} catch (ParseException e) {
			return true;
		}
	}
}
