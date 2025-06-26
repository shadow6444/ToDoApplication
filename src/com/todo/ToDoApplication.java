/*
 * Program: To-Do Application
 * Description: This application helps the user to add,view,delete and update task status. User can also sort the task by date.
 * 				first user need to add title and date of correct format and then user can see the task added below in task panel, 
 * 				then user can perform the other CRUD operation on it and exit the app.
 * Author: L1F22BSSE0300 - Anas Akram But, L1F22BSSEO3016 - Fahad Shahbaz
 * Input: title (string), date (date)
 * Output: list of tasks (pending/completed)
 * Result: User can do CRUD operations with the todo application
 * */

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

	/**
	 * Launch the application.
	 */

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

	/**
	 * Create the application.
	 */

	public ToDoApplication() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */

	private void initialize() {
		frame = new JFrame("To-Do List Application");
		frame.setBounds(100, 100, 600, 550);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout());

		// modal for the list of tasks
		taskArray = new ArrayList<>();
		taskListModel = new DefaultListModel<>();
		taskJList = new JList<>(taskListModel);
		taskJList.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		taskJList.setBackground(new Color(245, 245, 245));
		taskJList.setForeground(Color.BLACK);
		taskJList.setSelectionBackground(new Color(173, 216, 230));
		taskJList.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "Task List", 0,
				0, new Font("Segoe UI", Font.BOLD, 16)));

		// Panel where the user add inputs
		JPanel inputPanel = new JPanel();
		inputPanel.setLayout(new GridLayout(3, 2, 10, 5));
		inputPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		inputPanel.setBackground(new Color(230, 240, 255));

		// title inut field
		JLabel titleLabel = new JLabel("Task Title:");
		titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
		inputPanel.add(titleLabel);

		titleField = new JTextField();
		titleField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		inputPanel.add(titleField);

		// date input field
		JLabel dateLabel = new JLabel("Due Date (YYYY-MM-DD):");
		dateLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
		inputPanel.add(dateLabel);

		dueDateField = new JTextField();
		dueDateField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		inputPanel.add(dueDateField);

		// button to add task
		JButton addButton = new JButton("Add Task");
		styleButton(addButton);
		addButton.addActionListener(e -> addTask());
		inputPanel.add(addButton);

		// button to sort task by date
		JButton sortButton = new JButton("Sort by Due Date");
		sortButton.setForeground(new Color(0, 128, 192));
		styleButton(sortButton);
		sortButton.addActionListener(e -> sortTasks());
		inputPanel.add(sortButton);

		frame.getContentPane().add(inputPanel, BorderLayout.NORTH);

		// Panel where the list of task is displayed
		JScrollPane scrollPane = new JScrollPane(taskJList);
		scrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

		// Panel where the butotns are displayed
		JPanel buttonPanel = new JPanel();
		buttonPanel.setBackground(new Color(240, 248, 255));
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

		// button to toggle between done and pending task
		JButton toggleDoneButton = new JButton("Toggle Done");
		toggleDoneButton.setForeground(new Color(0, 128, 192));
		styleButton(toggleDoneButton);
		toggleDoneButton.addActionListener(e -> toggleTaskDone());
		buttonPanel.add(toggleDoneButton);
		
		// button to delete task
		JButton deleteButton = new JButton("Delete Task");
		deleteButton.setForeground(new Color(0, 128, 192));
		styleButton(deleteButton);
		deleteButton.addActionListener(e -> deleteTask());
		buttonPanel.add(deleteButton);

		// button to show only pending task
		JButton showPendingButton = new JButton("Show Pending");
		showPendingButton.setForeground(new Color(0, 128, 192));
		styleButton(showPendingButton);
		showPendingButton.addActionListener(e -> showPendingTasks());
		buttonPanel.add(showPendingButton);

		// button to show only comlpeted task
		JButton showCompletedButton = new JButton("Show Completed");
		showCompletedButton.setForeground(new Color(0, 128, 192));
		styleButton(showCompletedButton);
		showCompletedButton.addActionListener(e -> showCompletedTasks());
		buttonPanel.add(showCompletedButton);

		// button to show all task
		JButton showAllButton = new JButton("Show All");
		showAllButton.setForeground(new Color(0, 128, 192));
		styleButton(showAllButton);
		showAllButton.addActionListener(e -> showAllTasks());
		buttonPanel.add(showAllButton);

		// button to exit the system
		JButton exitButton = new JButton("Exit App");
		exitButton.setForeground(new Color(0, 128, 192));
		styleButton(exitButton);
		exitButton.addActionListener(e -> exitApp());
		buttonPanel.add(exitButton);

		frame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
	}

	// styling of button are same so for reusability
	private void styleButton(JButton button) {
		button.setBackground(new Color(70, 130, 180));
		button.setForeground(new Color(0, 128, 192));
		button.setFocusPainted(false);
		button.setFont(new Font("Segoe UI", Font.BOLD, 16));
		button.setPreferredSize(new Dimension(165, 35));
	}

	// function to add task to array
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

	// method to show all task
	private void showAllTasks() {
		taskListModel.clear();
		for (Task task : taskArray) {
			taskListModel.addElement(task);
		}
	}

	// method to show completed task
	private void showCompletedTasks() {
		taskListModel.clear();
		for (Task task : taskArray) {
			if (task.isCompleted) {
				taskListModel.addElement(task);
			}
		}
	}

	// method to show pending task
	private void showPendingTasks() {
		taskListModel.clear();
		for (Task task : taskArray) {
			if (!task.isCompleted) {
				taskListModel.addElement(task);
			}
		}
	}

	// method to toggle between done and pending task
	private void toggleTaskDone() {
		Task selectedTask = taskJList.getSelectedValue();
		if (selectedTask != null) {
			selectedTask.isCompleted = !selectedTask.isCompleted;
			taskJList.repaint();
		}
	}

	// method to delete task and show confirmation dialog
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

	// method to sort task by date
	private void sortTasks() {
		Collections.sort(taskArray, Comparator.comparing(t -> t.dueDate));
		showAllTasks();
	}

	// method to exit app
	private void exitApp() {
		int confirm = JOptionPane.showConfirmDialog(frame, "Are you sure you want to exit?", "Exit Confirmation",
				JOptionPane.YES_NO_OPTION);
		if (confirm == JOptionPane.YES_OPTION) {
			System.exit(0);
		}
	}

	// method to check id the date format is valid
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

	// method to check d date is of past
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
