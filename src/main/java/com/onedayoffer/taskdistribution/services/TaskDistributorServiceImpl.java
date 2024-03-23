package com.onedayoffer.taskdistribution.services;

import com.onedayoffer.taskdistribution.DTO.EmployeeDTO;
import com.onedayoffer.taskdistribution.DTO.TaskDTO;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component
public class TaskDistributorServiceImpl implements TaskDistributor{

    private int minTime = 360;
    private int maxTime = 420;

    @Override
    public void distribute(List<EmployeeDTO> employees, List<TaskDTO> tasks) throws Exception {
        //task - task with priority and leadtime
        //employess - with tasks - getTotalLeadTime()
        List<TaskDTO> tasksByPriority = tasks.stream()
                .sorted(Comparator.comparing(TaskDTO::getPriority))
                .toList();

        while(tasks.size() != 0 && haveFreeEmployee(employees)) {
            int employeeIndex = getMostFreeGuy(employees);
            List<TaskDTO> oldTasks = employees.get(employeeIndex).getTasks();
            TaskDTO newTask = tasks.get(0);
            oldTasks.add(newTask);
            employees.get(employeeIndex).setTasks(oldTasks);

            if(tasks.size() != 0) {
                tasks.remove(0);
            }
        }

    }

    private int getMostFreeGuy(List<EmployeeDTO> employees) throws Exception {
        return employees.indexOf(employees.stream()
                .min(Comparator.comparing(EmployeeDTO::getTotalLeadTime))
                .orElseThrow(Exception::new));
    }

    private boolean haveFreeEmployee(List<EmployeeDTO> employees) {
        for (EmployeeDTO employee: employees) {
            if (employee.getTotalLeadTime() < maxTime) {
                return true;
            }
        }
        return false;
    }

    //todo проверка, что у сотрудника осталось достаточно времени, чтобы добавить таску
    private boolean isPossibleToAddTask(){
        return false;
    }
}
