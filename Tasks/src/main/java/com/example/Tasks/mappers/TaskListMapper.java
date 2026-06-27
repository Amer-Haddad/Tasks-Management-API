package com.example.Tasks.mappers;

import com.example.Tasks.domain.dto.TaskListDto;
import com.example.Tasks.domain.entities.TaskList;

public interface TaskListMapper {

    TaskList fromDto(TaskListDto taskListDto);

    TaskListDto toDto(TaskList taskList);
}
