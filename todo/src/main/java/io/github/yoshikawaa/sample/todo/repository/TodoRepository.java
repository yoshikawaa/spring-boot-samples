package io.github.yoshikawaa.sample.todo.repository;

import java.util.Collection;
import java.util.Optional;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import io.github.yoshikawaa.sample.todo.domain.Todo;

@Mapper
public interface TodoRepository {

    @Select("SELECT todo_id, todo_title, finished, created_at FROM todo WHERE todo_id = #{todoId}")
    // map-underscore-to-camel-caseプロパティをセットしているので、実際には@Results不要
    // @formatter:off
    @Results(id = "todo", value = {
            @Result(column = "todo_id", property = "todoId"),
            @Result(column = "todo_title", property = "todoTitle"),
            @Result(column = "finished", property = "finished"),
            @Result(column = "created_at", property = "createdAt") })
    // @formatter:on
    Optional<Todo> findById(String todoId);

    @Select("SELECT todo_id, todo_title, finished, created_at FROM todo")
    // map-underscore-to-camel-caseプロパティをセットしているので、実際には@ResultMap不要
    @ResultMap("todo")
    Collection<Todo> findAll();

    @Insert("INSERT INTO todo (todo_title, finished, created_at) VALUES (#{todoTitle}, #{finished}, #{createdAt})")
    @Options(useGeneratedKeys = true, keyProperty = "todoId")
    void create(Todo todo);

    @Update("UPDATE todo SET todo_title = #{todoTitle}, finished = #{finished}, created_at = #{createdAt} WHERE todo_id = #{todoId}")
    boolean updateById(Todo todo);

    @Delete("DELETE FROM todo WHERE todo_id = #{todoId}")
    void deleteById(Todo todo);

    @Select("SELECT COUNT(*) FROM todo WHERE finished = #{finished}")
    long countByFinished(boolean finished);

// 動的SQLはSQLで実装して@SelectProviderで使用する
//    public class TodoSelectProvider {
//        public String find(String todoId) {
//            return new SQL() {{
//                SELECT("todo_id", "todo_title", "finished", "created_at");
//                FROM("todo");
//                if (todoId != null) {
//                    WHERE("todo_id = #{todoId}");
//                }
//            }}.toString();
//        }
//    }
}
