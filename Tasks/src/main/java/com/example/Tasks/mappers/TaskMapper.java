package com.example.Tasks.mappers;

import com.example.Tasks.domain.dto.TaskDto;
import com.example.Tasks.domain.entities.Task;

public interface TaskMapper {

    Task fromDto(TaskDto taskDto);

    TaskDto toDto(Task task);

}
