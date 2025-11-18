package bg.senpai_main.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "watch_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WatchHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    // 👤 кой user гледа
    @ManyToOne(optional = false)
    @JoinColumn(name = "member_id")
    private Member member;

    // 🎬 кое аниме гледа
    @ManyToOne(optional = false)
    @JoinColumn(name = "episode_id")
    private Episode episode;

    // 🕒 кога user-ът е гледал анимето
    @Column(nullable = false)
    private LocalDateTime updatedOn = LocalDateTime.now();
}
