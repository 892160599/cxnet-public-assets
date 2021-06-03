package org.camunda.bpm.engine.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.camunda.bpm.engine.BadUserRequestException;
import org.camunda.bpm.engine.ProcessEngineException;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.impl.cmd.AddCommentCmd;
import org.camunda.bpm.engine.impl.cmd.AddGroupIdentityLinkCmd;
import org.camunda.bpm.engine.impl.cmd.AddUserIdentityLinkCmd;
import org.camunda.bpm.engine.impl.cmd.AssignTaskCmd;
import org.camunda.bpm.engine.impl.cmd.ClaimTaskCmd;
import org.camunda.bpm.engine.impl.cmd.CompleteTaskCmd;
import org.camunda.bpm.engine.impl.cmd.CreateAttachmentCmd;
import org.camunda.bpm.engine.impl.cmd.CreateTaskCmd;
import org.camunda.bpm.engine.impl.cmd.DelegateTaskCmd;
import org.camunda.bpm.engine.impl.cmd.DeleteAttachmentCmd;
import org.camunda.bpm.engine.impl.cmd.DeleteGroupIdentityLinkCmd;
import org.camunda.bpm.engine.impl.cmd.DeleteTaskAttachmentCmd;
import org.camunda.bpm.engine.impl.cmd.DeleteTaskCmd;
import org.camunda.bpm.engine.impl.cmd.DeleteUserIdentityLinkCmd;
import org.camunda.bpm.engine.impl.cmd.GetAttachmentCmd;
import org.camunda.bpm.engine.impl.cmd.GetAttachmentContentCmd;
import org.camunda.bpm.engine.impl.cmd.GetIdentityLinksForTaskCmd;
import org.camunda.bpm.engine.impl.cmd.GetProcessInstanceAttachmentsCmd;
import org.camunda.bpm.engine.impl.cmd.GetProcessInstanceCommentsCmd;
import org.camunda.bpm.engine.impl.cmd.GetSubTasksCmd;
import org.camunda.bpm.engine.impl.cmd.GetTaskAttachmentCmd;
import org.camunda.bpm.engine.impl.cmd.GetTaskAttachmentContentCmd;
import org.camunda.bpm.engine.impl.cmd.GetTaskAttachmentsCmd;
import org.camunda.bpm.engine.impl.cmd.GetTaskCommentCmd;
import org.camunda.bpm.engine.impl.cmd.GetTaskCommentsCmd;
import org.camunda.bpm.engine.impl.cmd.GetTaskEventsCmd;
import org.camunda.bpm.engine.impl.cmd.GetTaskVariableCmd;
import org.camunda.bpm.engine.impl.cmd.GetTaskVariableCmdTyped;
import org.camunda.bpm.engine.impl.cmd.GetTaskVariablesCmd;
import org.camunda.bpm.engine.impl.cmd.PatchTaskVariablesCmd;
import org.camunda.bpm.engine.impl.cmd.RemoveTaskVariablesCmd;
import org.camunda.bpm.engine.impl.cmd.ResolveTaskCmd;
import org.camunda.bpm.engine.impl.cmd.SaveAttachmentCmd;
import org.camunda.bpm.engine.impl.cmd.SaveTaskCmd;
import org.camunda.bpm.engine.impl.cmd.SetTaskOwnerCmd;
import org.camunda.bpm.engine.impl.cmd.SetTaskPriorityCmd;
import org.camunda.bpm.engine.impl.cmd.SetTaskVariablesCmd;
import org.camunda.bpm.engine.impl.interceptor.CommandExecutor;
import org.camunda.bpm.engine.impl.util.EnsureUtil;
import org.camunda.bpm.engine.impl.util.ExceptionUtil;
import org.camunda.bpm.engine.task.Attachment;
import org.camunda.bpm.engine.task.Comment;
import org.camunda.bpm.engine.task.Event;
import org.camunda.bpm.engine.task.IdentityLink;
import org.camunda.bpm.engine.task.NativeTaskQuery;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.task.TaskQuery;
import org.camunda.bpm.engine.task.TaskReport;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.value.TypedValue;

public class TaskServiceImpl
        extends ServiceImpl
        implements TaskService {
    @Override
    public Task newTask() {
        return newTask(null);
    }

    @Override
    public Task newTask(String taskId) {
        return (Task) this.commandExecutor.execute(new CreateTaskCmd(taskId));
    }

    @Override
    public void saveTask(Task task) {
        this.commandExecutor.execute(new SaveTaskCmd(task));
    }

    @Override
    public void deleteTask(String taskId) {
        this.commandExecutor.execute(new DeleteTaskCmd(taskId, null, false));
    }

    @Override
    public void deleteTasks(Collection<String> taskIds) {
        this.commandExecutor.execute(new DeleteTaskCmd(taskIds, null, false));
    }

    @Override
    public void deleteTask(String taskId, boolean cascade) {
        this.commandExecutor.execute(new DeleteTaskCmd(taskId, null, cascade));
    }

    @Override
    public void deleteTasks(Collection<String> taskIds, boolean cascade) {
        this.commandExecutor.execute(new DeleteTaskCmd(taskIds, null, cascade));
    }

    @Override
    public void deleteTask(String taskId, String deleteReason) {
        this.commandExecutor.execute(new DeleteTaskCmd(taskId, deleteReason, false));
    }

    @Override
    public void deleteTasks(Collection<String> taskIds, String deleteReason) {
        this.commandExecutor.execute(new DeleteTaskCmd(taskIds, deleteReason, false));
    }

    @Override
    public void setAssignee(String taskId, String userId) {
        this.commandExecutor.execute(new AssignTaskCmd(taskId, userId));
    }

    @Override
    public void setOwner(String taskId, String userId) {
        this.commandExecutor.execute(new SetTaskOwnerCmd(taskId, userId));
    }

    @Override
    public void addCandidateUser(String taskId, String userId) {
        this.commandExecutor.execute(new AddUserIdentityLinkCmd(taskId, userId, "candidate"));
    }

    @Override
    public void addCandidateGroup(String taskId, String groupId) {
        this.commandExecutor.execute(new AddGroupIdentityLinkCmd(taskId, groupId, "candidate"));
    }

    @Override
    public void addUserIdentityLink(String taskId, String userId, String identityLinkType) {
        this.commandExecutor.execute(new AddUserIdentityLinkCmd(taskId, userId, identityLinkType));
    }

    @Override
    public void addGroupIdentityLink(String taskId, String groupId, String identityLinkType) {
        this.commandExecutor.execute(new AddGroupIdentityLinkCmd(taskId, groupId, identityLinkType));
    }

    @Override
    public void deleteCandidateGroup(String taskId, String groupId) {
        this.commandExecutor.execute(new DeleteGroupIdentityLinkCmd(taskId, groupId, "candidate"));
    }

    @Override
    public void deleteCandidateUser(String taskId, String userId) {
        this.commandExecutor.execute(new DeleteUserIdentityLinkCmd(taskId, userId, "candidate"));
    }

    @Override
    public void deleteGroupIdentityLink(String taskId, String groupId, String identityLinkType) {
        this.commandExecutor.execute(new DeleteGroupIdentityLinkCmd(taskId, groupId, identityLinkType));
    }

    @Override
    public void deleteUserIdentityLink(String taskId, String userId, String identityLinkType) {
        this.commandExecutor.execute(new DeleteUserIdentityLinkCmd(taskId, userId, identityLinkType));
    }

    @Override
    public List<IdentityLink> getIdentityLinksForTask(String taskId) {
        return (List) this.commandExecutor.execute(new GetIdentityLinksForTaskCmd(taskId));
    }

    @Override
    public void claim(String taskId, String userId) {
        this.commandExecutor.execute(new ClaimTaskCmd(taskId, userId));
    }

    @Override
    public void complete(String taskId) {
        this.commandExecutor.execute(new CompleteTaskCmd(taskId, null));
    }

    @Override
    public void complete(String taskId, Map<String, Object> variables) {
        this.commandExecutor.execute(new CompleteTaskCmd(taskId, variables));
    }

    @Override
    public void delegateTask(String taskId, String userId) {
        this.commandExecutor.execute(new DelegateTaskCmd(taskId, userId));
    }

    @Override
    public void resolveTask(String taskId) {
        this.commandExecutor.execute(new ResolveTaskCmd(taskId, null));
    }

    @Override
    public void resolveTask(String taskId, Map<String, Object> variables) {
        this.commandExecutor.execute(new ResolveTaskCmd(taskId, variables));
    }

    @Override
    public void setPriority(String taskId, int priority) {
        this.commandExecutor.execute(new SetTaskPriorityCmd(taskId, priority));
    }

    @Override
    public TaskQuery createTaskQuery() {
        return new TaskQueryImpl(this.commandExecutor);
    }

    @Override
    public NativeTaskQuery createNativeTaskQuery() {
        return new NativeTaskQueryImpl(this.commandExecutor);
    }

    @Override
    public VariableMap getVariables(String executionId) {
        return getVariablesTyped(executionId);
    }

    @Override
    public VariableMap getVariablesTyped(String executionId) {
        return getVariablesTyped(executionId, true);
    }

    @Override
    public VariableMap getVariablesTyped(String taskId, boolean deserializeValues) {
        return (VariableMap) this.commandExecutor.execute(new GetTaskVariablesCmd(taskId, null, false, deserializeValues));
    }

    @Override
    public VariableMap getVariablesLocal(String taskId) {
        return getVariablesLocalTyped(taskId);
    }

    @Override
    public VariableMap getVariablesLocalTyped(String taskId) {
        return getVariablesLocalTyped(taskId, true);
    }

    @Override
    public VariableMap getVariablesLocalTyped(String taskId, boolean deserializeValues) {
        return (VariableMap) this.commandExecutor.execute(new GetTaskVariablesCmd(taskId, null, true, deserializeValues));
    }

    @Override
    public VariableMap getVariables(String executionId, Collection<String> variableNames) {
        return getVariablesTyped(executionId, variableNames, true);
    }

    @Override
    public VariableMap getVariablesTyped(String executionId, Collection<String> variableNames, boolean deserializeValues) {
        return (VariableMap) this.commandExecutor.execute(new GetTaskVariablesCmd(executionId, variableNames, false, deserializeValues));
    }

    @Override
    public VariableMap getVariablesLocal(String executionId, Collection<String> variableNames) {
        return getVariablesLocalTyped(executionId, variableNames, true);
    }

    @Override
    public VariableMap getVariablesLocalTyped(String executionId, Collection<String> variableNames, boolean deserializeValues) {
        return (VariableMap) this.commandExecutor.execute(new GetTaskVariablesCmd(executionId, variableNames, true, deserializeValues));
    }

    @Override
    public Object getVariable(String executionId, String variableName) {
        return this.commandExecutor.execute(new GetTaskVariableCmd(executionId, variableName, false));
    }

    @Override
    public Object getVariableLocal(String executionId, String variableName) {
        return this.commandExecutor.execute(new GetTaskVariableCmd(executionId, variableName, true));
    }

    @Override
    public <T extends TypedValue> T getVariableTyped(String taskId, String variableName) {
        return getVariableTyped(taskId, variableName, false, true);
    }

    @Override
    public <T extends TypedValue> T getVariableTyped(String taskId, String variableName, boolean deserializeValue) {
        return getVariableTyped(taskId, variableName, false, deserializeValue);
    }

    @Override
    public <T extends TypedValue> T getVariableLocalTyped(String taskId, String variableName) {
        return getVariableTyped(taskId, variableName, true, true);
    }

    @Override
    public <T extends TypedValue> T getVariableLocalTyped(String taskId, String variableName, boolean deserializeValue) {
        return getVariableTyped(taskId, variableName, true, deserializeValue);
    }

    protected <T extends TypedValue> T getVariableTyped(String taskId, String variableName, boolean isLocal, boolean deserializeValue) {
        return (T) this.commandExecutor.execute(new GetTaskVariableCmdTyped(taskId, variableName, isLocal, deserializeValue));
    }

    @Override
    public void setVariable(String executionId, String variableName, Object value) {
        EnsureUtil.ensureNotNull("variableName", variableName);
        Map<String, Object> variables = new HashMap<>(2);
        variables.put(variableName, value);
        setVariables(executionId, variables, false);
    }

    @Override
    public void setVariableLocal(String executionId, String variableName, Object value) {
        EnsureUtil.ensureNotNull("variableName", variableName);
        Map<String, Object> variables = new HashMap<>(2);
        variables.put(variableName, value);
        setVariables(executionId, variables, true);
    }

    @Override
    public void setVariables(String executionId, Map<String, ? extends Object> variables) {
        setVariables(executionId, variables, false);
    }

    @Override
    public void setVariablesLocal(String executionId, Map<String, ? extends Object> variables) {
        setVariables(executionId, variables, true);
    }

    protected void setVariables(String executionId, Map<String, ? extends Object> variables, boolean local) {
        try {
            this.commandExecutor.execute(new SetTaskVariablesCmd(executionId, variables, local));
        } catch (ProcessEngineException ex) {
            if (ExceptionUtil.checkValueTooLongException(ex)) {
                throw new BadUserRequestException("Variable value is too long", ex);
            }
            throw ex;
        }
    }

    public void updateVariablesLocal(String taskId, Map<String, ? extends Object> modifications, Collection<String> deletions) {
        updateVariables(taskId, modifications, deletions, true);
    }

    public void updateVariables(String taskId, Map<String, ? extends Object> modifications, Collection<String> deletions) {
        updateVariables(taskId, modifications, deletions, false);
    }

    protected void updateVariables(String taskId, Map<String, ? extends Object> modifications, Collection<String> deletions, boolean local) {
        try {
            this.commandExecutor.execute(new PatchTaskVariablesCmd(taskId, modifications, deletions, local));
        } catch (ProcessEngineException ex) {
            if (ExceptionUtil.checkValueTooLongException(ex)) {
                throw new BadUserRequestException("Variable value is too long", ex);
            }
            throw ex;
        }
    }

    @Override
    public void removeVariable(String taskId, String variableName) {
        Collection<String> variableNames = new ArrayList<>(1);
        variableNames.add(variableName);
        this.commandExecutor.execute(new RemoveTaskVariablesCmd(taskId, variableNames, false));
    }

    @Override
    public void removeVariableLocal(String taskId, String variableName) {
        Collection<String> variableNames = new ArrayList<>(1);
        variableNames.add(variableName);
        this.commandExecutor.execute(new RemoveTaskVariablesCmd(taskId, variableNames, true));
    }

    @Override
    public void removeVariables(String taskId, Collection<String> variableNames) {
        this.commandExecutor.execute(new RemoveTaskVariablesCmd(taskId, variableNames, false));
    }

    @Override
    public void removeVariablesLocal(String taskId, Collection<String> variableNames) {
        this.commandExecutor.execute(new RemoveTaskVariablesCmd(taskId, variableNames, true));
    }

    @Override
    public void addComment(String taskId, String processInstance, String message) {
        createComment(taskId, processInstance, message);
    }

    @Override
    public Comment createComment(String taskId, String processInstance, String message) {
        return (Comment) this.commandExecutor.execute(new AddCommentCmd(taskId, processInstance, message));
    }

    @Override
    public List<Comment> getTaskComments(String taskId) {
        return (List) this.commandExecutor.execute(new GetTaskCommentsCmd(taskId));
    }

    @Override
    public Comment getTaskComment(String taskId, String commentId) {
        return (Comment) this.commandExecutor.execute(new GetTaskCommentCmd(taskId, commentId));
    }

    @Override
    public List<Event> getTaskEvents(String taskId) {
        return (List) this.commandExecutor.execute(new GetTaskEventsCmd(taskId));
    }

    @Override
    public List<Comment> getProcessInstanceComments(String processInstanceId) {
        return (List) this.commandExecutor.execute(new GetProcessInstanceCommentsCmd(processInstanceId));
    }

    @Override
    public Attachment createAttachment(String attachmentType, String taskId, String processInstanceId, String attachmentName, String attachmentDescription, InputStream content) {
        return (Attachment) this.commandExecutor.execute(new CreateAttachmentCmd(attachmentType, taskId, processInstanceId, attachmentName, attachmentDescription, content, null));
    }

    @Override
    public Attachment createAttachment(String attachmentType, String taskId, String processInstanceId, String attachmentName, String attachmentDescription, String url) {
        return (Attachment) this.commandExecutor.execute(new CreateAttachmentCmd(attachmentType, taskId, processInstanceId, attachmentName, attachmentDescription, null, url));
    }

    @Override
    public InputStream getAttachmentContent(String attachmentId) {
        return (InputStream) this.commandExecutor.execute(new GetAttachmentContentCmd(attachmentId));
    }

    @Override
    public InputStream getTaskAttachmentContent(String taskId, String attachmentId) {
        return (InputStream) this.commandExecutor.execute(new GetTaskAttachmentContentCmd(taskId, attachmentId));
    }

    @Override
    public void deleteAttachment(String attachmentId) {
        this.commandExecutor.execute(new DeleteAttachmentCmd(attachmentId));
    }

    @Override
    public void deleteTaskAttachment(String taskId, String attachmentId) {
        this.commandExecutor.execute(new DeleteTaskAttachmentCmd(taskId, attachmentId));
    }

    @Override
    public Attachment getAttachment(String attachmentId) {
        return (Attachment) this.commandExecutor.execute(new GetAttachmentCmd(attachmentId));
    }

    @Override
    public Attachment getTaskAttachment(String taskId, String attachmentId) {
        return (Attachment) this.commandExecutor.execute(new GetTaskAttachmentCmd(taskId, attachmentId));
    }

    @Override
    public List<Attachment> getTaskAttachments(String taskId) {
        return (List) this.commandExecutor.execute(new GetTaskAttachmentsCmd(taskId));
    }

    @Override
    public List<Attachment> getProcessInstanceAttachments(String processInstanceId) {
        return (List) this.commandExecutor.execute(new GetProcessInstanceAttachmentsCmd(processInstanceId));
    }

    @Override
    public void saveAttachment(Attachment attachment) {
        this.commandExecutor.execute(new SaveAttachmentCmd(attachment));
    }

    @Override
    public List<Task> getSubTasks(String parentTaskId) {
        return (List) this.commandExecutor.execute(new GetSubTasksCmd(parentTaskId));
    }

    @Override
    public TaskReport createTaskReport() {
        return new TaskReportImpl(this.commandExecutor);
    }
}
