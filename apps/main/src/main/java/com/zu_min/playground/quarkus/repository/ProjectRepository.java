package com.zu_min.playground.quarkus.repository;

import javax.enterprise.context.ApplicationScoped;

import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase;

import com.zu_min.playground.quarkus.entity.Project;

/**
 * プロジェクト情報関連処理。
 */
@ApplicationScoped
public class ProjectRepository implements PanacheRepositoryBase<Project, Long> {

}
