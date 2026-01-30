import { PostCard } from '@/components/PostCard';
import { createSnippet, type PostSummary } from '@/lib/posts';
import clsx from 'clsx';

const accentTokens = {
  random: {
    border: 'border-amber-400/40 bg-amber-400/5',
    badge: 'text-amber-200',
    heading: 'text-white',
  },
  crypto: {
    border: 'border-sky-400/40 bg-sky-400/5',
    badge: 'text-sky-200',
    heading: 'text-white',
  },
};

type AccentVariant = keyof typeof accentTokens;

interface PostSectionProps {
  title: string;
  subtitle: string;
  posts: PostSummary[];
  accent: AccentVariant;
}

export const PostSection = ({ title, subtitle, posts, accent }: PostSectionProps) => {
  const accentClasses = accentTokens[accent];

  return (
    <section className="space-y-6">
      <header>
        <p className={clsx('text-xs uppercase tracking-[0.4em]', accentClasses.badge)}>
          {accent === 'random' ? 'Random' : 'Crypto'} Spotlight
        </p>
        <h2 className="text-4xl font-semibold text-white mt-2 mb-3">{title}</h2>
        <p className="text-base text-white/70 max-w-2xl">{subtitle}</p>
      </header>

      {posts.length ? (
        <div className="grid gap-6 md:grid-cols-2">
          {posts.map((post) => (
            <PostCard
              key={post.id}
              post={post}
              accentClasses={accentClasses}
              snippet={createSnippet(post.content)}
            />
          ))}
        </div>
      ) : (
        <div className="rounded-3xl border border-white/10 p-8 text-white/60 text-sm">
          No posts live for this category yet. Check back soon.
        </div>
      )}
    </section>
  );
};
