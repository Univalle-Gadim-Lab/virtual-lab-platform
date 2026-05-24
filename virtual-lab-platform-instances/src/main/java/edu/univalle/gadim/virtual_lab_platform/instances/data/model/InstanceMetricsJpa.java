package edu.univalle.gadim.virtual_lab_platform.instances.data.model;

import edu.univalle.gadim.virtual_lab_platform.instances.api.type.InstanceMetrics;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * JPA entity representing resource utilization metrics for a virtual lab instance.
 *
 * <p>Mapped to the {@code instance_metrics} table and implements the {@link InstanceMetrics}
 * domain contract. Stores point-in-time snapshots of CPU, memory, disk, and time usage.
 *
 * @see InstanceMetrics
 * @see InstanceMetricsRepository
 */
@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "instance_metrics")
public class InstanceMetricsJpa implements InstanceMetrics {

  @Id
  @Column(name = "id")
  private String id;

  @Column(name = "instance_id")
  private String instanceId;

  @Column(name = "current_cpu_usage")
  private Double currentCpuUsage;

  @Column(name = "current_memory_usage")
  private Double currentMemoryUsage;

  @Column(name = "current_disk_usage")
  private Double currentDiskUsage;

  @Column(name = "current_time_usage")
  private Double currentTimeUsage;

  @Override
  public String id() {
    return this.id;
  }

  @Override
  public String instanceId() {
    return this.instanceId;
  }

  @Override
  public double currentCpuUsage() {
    return this.currentCpuUsage;
  }

  @Override
  public double currentMemoryUsage() {
    return this.currentMemoryUsage;
  }

  @Override
  public double currentDiskUsage() {
    return this.currentDiskUsage;
  }

  @Override
  public double currentTimeUsage() {
    return this.currentTimeUsage;
  }

  @Override
  public final int hashCode() {
    return Objects.hash(getId());
  }
}