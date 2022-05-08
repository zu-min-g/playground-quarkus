package com.zu_min.playground.quarkus.resource.employee;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional;
import io.quarkus.panache.common.Page;

import com.zu_min.playground.quarkus.entity.Employee;
import com.zu_min.playground.quarkus.repository.EmployeeRepository;

import io.smallrye.mutiny.Uni;
import org.jboss.resteasy.reactive.NoCache;
import org.jboss.resteasy.reactive.RestQuery;

/**
 * 社員リソース。
 */
@Path("employee")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class EmployeeResource {
    @Inject
    EmployeeRepository employeeRepo;

    @Inject
    EmployeeMapper employeeMapper;

    @GET
    @Path("test")
    @NoCache
    @ReactiveTransactional
    public Uni<EmployeeDto> test() {
        var employee = new Employee();
        return employeeRepo.persist(employee).map(e -> employeeMapper.toDto(e));
    }

    /**
     * 社員一覧を返却します。
     */
    @GET
    @NoCache
    public Uni<List<EmployeeDto>> list(@RestQuery @DefaultValue("0") int index) {
        Page page = Page.of(index, 12);
        return employeeRepo.findAll()
                .page(page).list()
                .map(l -> employeeMapper.toDto(l));
    }
}
