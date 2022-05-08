package com.zu_min.playground.quarkus.resource.department;

import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional;
import io.quarkus.panache.common.Page;

import com.zu_min.playground.quarkus.repository.DepartmentRepository;
import com.zu_min.playground.quarkus.repository.EmployeeRepository;
import com.zu_min.playground.quarkus.repository.ProjectRepository;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.jboss.resteasy.reactive.NoCache;
import org.jboss.resteasy.reactive.RestQuery;

/**
 * 部門情報リソース。
 */
@Path("department")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class DepartmentResource {
    @Inject
    DepartmentMapper departmentMapper;

    @Inject
    DepartmentRepository departmentRepo;

    @Inject
    EmployeeRepository employeeRepo;

    @Inject
    ProjectRepository projectRepo;

    /**
     * 部門情報一覧を返却します。
     */
    @GET
    @NoCache
    public Uni<List<DepartmentDto>> list(@RestQuery @DefaultValue("0") int index) {
        return departmentRepo.find("from Department d "
                + " left join fetch d.employees e ")
                .page(Page.of(index, 12)).list()
                .call(l -> employeeRepo.find("from Employee e "
                        + " left join fetch e.projects p "
                        + " where e in ?1",
                        l.stream().flatMap(i -> i.getEmployees().stream())
                                .distinct().collect(Collectors.toList()))
                        .list()
                        .map(a -> a)
                        .onItem().ignore().andContinueWithNull())
                .map(l -> departmentMapper.toDto(l));
    }

    /**
     * 部門情報を追加します。
     */
    @POST
    @ReactiveTransactional
    public Uni<DepartmentDto> add(@Valid DepartmentDto department) {
        var entity = departmentMapper.toEntity(department);
        return departmentRepo.persist(entity).call(d -> {
            return Multi.createFrom().items(d.getEmployees().stream())
                    .invoke(e -> e.setDepartment(entity))
                    .call(e -> employeeRepo.persist(e))
                    .call(e -> {
                        return Multi.createFrom().items(e.getProjects().stream())
                                .invoke(p -> p.getEmployees().add(e))
                                .call(p -> projectRepo.persist(p))
                                .collect().last();
                    })
                    .collect().last();
        }).map(e -> departmentMapper.toDto(e));
    }
}
