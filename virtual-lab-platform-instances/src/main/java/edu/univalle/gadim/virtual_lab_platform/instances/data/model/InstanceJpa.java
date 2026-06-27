package edu.univalle.gadim.virtual_lab_platform.instances.data.model;

import edu.univalle.gadim.virtual_lab_platform.instances.api.type.Instance;
import edu.univalle.gadim.virtual_lab_platform.instances.api.type.InstanceStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * JPA entity representing a virtual lab instance.
 *
 * <p>Mapped to the {@code instances} table and implements the {@link Instance} domain
 * contract. Stores container configuration, networking details, lifecycle timestamps,
 * and runtime status.
 *
 * <p><b>Thread Safety:</b> This class is not thread-safe. Instances should not
 * be shared across threads without external synchronization.
 *
 * @see Instance
 * @see InstanceStatus
 * @see InstanceRepository
 */
@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "instances")
public class InstanceJpa implements Instance {

  @Id
  @Column(name = "id")
  private String id;

  @Column(name = "name")
  private String name;

  @Column(name = "description")
  private String description;

  @Column(name = "external_ip", length = 64)
  private String externalIp;

  @Column(name = "image_name")
  private String imageName;

  @Column(name = "image_version")
  private String imageVersion;

  @Column(name = "image_registry")
  private String imageRegistry;

  @Column(name = "cpu_cores")
  private Integer cpuCores;

  @Column(name = "memory_mb")
  private Integer memoryMb;

  @Column(name = "storage_mb")
  private Integer storageMb;

  @Column(name = "gpu_enabled")
  private Boolean gpuEnabled;

  @Column(name = "exposed_port")
  private Integer exposedPort;

  @Column(name = "internal_ip")
  private String internalIp;

  @Column(name = "vnc_port")
  private Integer vncPort;

  @Column(name = "vnc_enabled")
  private Boolean vncEnabled;

  @Column(name = "vnc_password")
  private String vncPassword;

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @Column(name = "expires_at")
  private LocalDateTime expiresAt;

  @Column(name = "started_at")
  private LocalDateTime startedAt;

  @Column(name = "stopped_at")
  private LocalDateTime stoppedAt;

  @Column(name = "deleted_at")
  private LocalDateTime deletedAt;

  @Column(name = "last_accessed_at")
  private LocalDateTime lastAccessedAt;

  @Enumerated(EnumType.STRING)
  @Column(name = "status")
  private InstanceStatus status;

  @Override
  public String id() {
    return this.id;
  }

  @Override
  public String name() {
    return this.name;
  }

  @Override
  public Optional<String> description() {
    return Optional.ofNullable(this.description);
  }

  @Override
  public String externalIp() {
    return this.externalIp;
  }

  @Override
  public String imageName() {
    return this.imageName;
  }

  @Override
  public String imageVersion() {
    return this.imageVersion;
  }

  @Override
  public Optional<LocalDateTime> stoppedAt() {
    return Optional.ofNullable(this.stoppedAt);
  }

  @Override
  public Optional<LocalDateTime> deletedAt() {
    return Optional.ofNullable(this.deletedAt);
  }

  @Override
  public Optional<LocalDateTime> lastAccessedAt() {
    return Optional.ofNullable(this.lastAccessedAt);
  }

  @Override
  public LocalDateTime createdAt() {
    return this.createdAt;
  }

  @Override
  public LocalDateTime expiresAt() {
    return this.expiresAt;
  }

  @Override
  public LocalDateTime startedAt() {
    return this.startedAt;
  }

  @Override
  public int cpuCores() {
    return this.cpuCores;
  }

  @Override
  public String imageRegistry() {
    return this.imageRegistry;
  }

  @Override
  public int memoryMb() {
    return this.memoryMb;
  }

  @Override
  public int storageMb() {
    return this.storageMb;
  }

  @Override
  public boolean gpuEnabled() {
    return this.gpuEnabled;
  }

  @Override
  public int exposedPort() {
    return this.exposedPort;
  }

  @Override
  public int vncPort() {
    return this.vncPort;
  }

  @Override
  public boolean vncEnabled() {
    return this.vncEnabled;
  }

  @Override
  public String vncPassword() {
    return this.vncPassword;
  }

  @Override
  public String internalIp() {
    return this.internalIp;
  }

  @Override
  public InstanceStatus status() {
    return this.status;
  }

  @Override
  public final boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InstanceJpa instanceJpa = (InstanceJpa) o;
    return getId() != null && Objects.equals(getId(), instanceJpa.getId());
  }

  @Override
  public final int hashCode() {
    return Objects.hash(getId());
  }
}
