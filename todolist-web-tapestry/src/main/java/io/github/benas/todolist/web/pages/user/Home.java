/*
 * The MIT License
 *
 * Copyright (c) 2013, benas (md.benhassine@gmail.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package io.github.benas.todolist.web.pages.user;

import io.github.todolist.core.domain.Priority;
import io.github.todolist.core.domain.Status;
import io.github.todolist.core.domain.Todo;
import io.github.todolist.core.domain.User;
import io.github.todolist.core.service.api.TodoService;
import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.ioc.annotations.Inject;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @author benas (md.benhassine@gmail.com)
 */
@SuppressWarnings("unused")
public class Home {

    @Inject
    private TodoService todoService;

    @SessionState
    private User loggedUser;

    @Property
    private List<Todo> todoList;

    @Property
    private Todo currentTodo;

    @Property
    private int doneCount;

    @Property
    private int todoCount;

    @Property
    private int totalCount;

    @OnEvent(value = EventConstants.ACTIVATE)
    public void init() {
        Long loggedUserId = loggedUser.getId();
        todoList = todoService.getTodoListByUser(loggedUserId);
        totalCount = todoList.size();
        doneCount = todoService.getTodoListByStatus(loggedUserId, Status.DONE).size();
        todoCount = todoService.getTodoListByStatus(loggedUserId, Status.TODO).size();
    }

    @OnEvent(value=EventConstants.ACTION, component="deleteTodoLink")
    public void deleteTodo(long todoId){
        Todo todo = todoService.getTodoById(todoId);
        if (todo != null){
            todoService.remove(todo);
        }
    }

    public String getCurrentStatusLabel() {
        return currentTodo.getStatus().equals(Status.DONE) ? "label-success" : "";
    }

    public String getCurrentPriorityIcon() {
        String priorityIcon = "";
        if (currentTodo.getPriority().equals(Priority.HIGH)) {
            priorityIcon = "up";
        } else if (currentTodo.getPriority().equals(Priority.MEDIUM)) {
            priorityIcon = "right";
        } else if (currentTodo.getPriority().equals(Priority.LOW)) {
            priorityIcon = "down";
        }
        return priorityIcon;
    }

    public String getCurrentDueDate() {
        return new SimpleDateFormat("dd/MM/yyyy").format(currentTodo.getDueDate());
    }

}
