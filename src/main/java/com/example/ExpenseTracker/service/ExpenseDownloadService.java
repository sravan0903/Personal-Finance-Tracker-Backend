package com.example.ExpenseTracker.service;

public interface ExpenseDownloadService {
	byte[] generateExpenseExcel(String email);
}
