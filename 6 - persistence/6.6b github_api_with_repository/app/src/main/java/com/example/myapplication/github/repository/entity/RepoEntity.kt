package com.example.myapplication.github.repository.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.myapplication.github.usecase.model.GithubRepo

@Entity(tableName = "repo_table")
data class RepoEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "name") val name: String
)

fun RepoEntity.toModel(): GithubRepo {
    return GithubRepo(id = this.id, name = this.name)
}

fun GithubRepo.toEntity(): RepoEntity {
    return RepoEntity(id = this.id, name = this.name)
}