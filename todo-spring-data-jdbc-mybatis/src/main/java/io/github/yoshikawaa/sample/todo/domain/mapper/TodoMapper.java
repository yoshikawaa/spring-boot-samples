package io.github.yoshikawaa.sample.todo.domain.mapper;

import java.util.Collection;
import java.util.Optional;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.data.jdbc.mybatis.MyBatisContext;

import io.github.yoshikawaa.sample.todo.domain.Todo;

@Mapper
public interface TodoMapper {

    @Select("SELECT todo_id, todo_title, finished, created_at FROM todo WHERE todo_id = #{id}")
    Optional<Todo> findById(MyBatisContext context);

    @Select("SELECT todo_id, todo_title, finished, created_at FROM todo")
    Collection<Todo> findAll();

    @Insert("INSERT INTO todo (todo_title, finished, created_at) VALUES (#{instance.todoTitle}, #{instance.finished}, #{instance.createdAt})")
    @Options(useGeneratedKeys = true, keyProperty = "instance.todoId")
    Todo insert(MyBatisContext context);

    @Update("UPDATE todo SET todo_title = #{instance.todoTitle}, finished = #{instance.finished}, created_at = #{instance.createdAt} WHERE todo_id = #{id}")
    Todo update(MyBatisContext context);

    @Delete("DELETE FROM todo WHERE todo_id = #{id}")
    void delete(MyBatisContext context);

    @Select("SELECT COUNT(*) FROM todo WHERE finished = 'FALSE'")
    long count(MyBatisContext context);
}
