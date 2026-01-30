import type { PostSummary } from '@/lib/posts';
import clsx from 'clsx';

const walletPreview = (wallet: string) => {
  if (wallet.length <= 10) {
    return wallet;
  }
  return `${wallet.slice(0, 6)}…${wallet.slice(-4)}`;
};

const formatDate = (isoDate: string) =>
  new Intl.DateTimeFormat('en', {
    month: 'short',
    day: 'numeric',
  }).format(new Date(isoDate));

interface PostCardProps {
  post: PostSummary;
  accentClasses: {
    border: string;
    badge: string;
    heading: string;
  };
  snippet: string;
}

export const PostCard = ({ post, accentClasses, snippet }: PostCardProps) => {
  return (
    <article
      className={clsx(
        'flex flex-col justify-between rounded-3xl border px-6 py-5 shadow-2xl shadow-black/20 backdrop-blur',
        accentClasses.border,
      )}
    >
      <header className="flex items-center justify-between text-xs uppercase tracking-[0.3em]">
        <span className={clsx('font-semibold', accentClasses.badge)}>
          {post.category.theme}
        </span>
        <span className="text-white/60">{formatDate(post.createdAt)}</span>
      </header>
      <div className="py-5">
        <h3 className={clsx('text-2xl font-semibold mb-3', accentClasses.heading)}>
          {post.title?.trim() || 'Untitled Post'}
        </h3>
        <p className="text-sm text-white/70 leading-relaxed">{snippet}</p>
      </div>
      <footer className="flex items-center justify-between text-xs uppercase tracking-[0.4em] text-white/60">
        <span>{post.author.username || 'Anonymous'}</span>
        <span>{walletPreview(post.author.walletAddress)}</span>
      </footer>
    </article>
  );
};
