import { AuthButton } from '@/components/AuthButton';
import { Page } from '@/components/PageLayout';
import { PostSection } from '@/components/PostSection';
import { fetchPosts, filterPostsByTheme, type PostSummary } from '@/lib/posts';
import Link from 'next/link';

export const dynamic = 'force-dynamic';

const loadVisiblePosts = async (): Promise<PostSummary[]> => {
  try {
    const posts = await fetchPosts();
    return posts.filter((post) => !post.deleted);
  } catch (error) {
    console.error('Failed to load posts', error);
    return [];
  }
};

export default async function CryptoPage() {
  const posts = await loadVisiblePosts();
  const cryptoPosts = filterPostsByTheme(posts, 'crypto');

  return (
    <Page className="bg-black text-white">
      <Page.Main className="!p-0 flex flex-col gap-16 bg-black text-white">
        <section className="w-full border-b border-white/10 bg-gradient-to-b from-black via-black to-[#02060f] px-6 py-16">
          <div className="mx-auto max-w-4xl space-y-6">
            <p className="text-xs uppercase tracking-[0.4em] text-white/60">
              On-chain intel • Wallet optional for browsing
            </p>
            <h1 className="text-5xl md:text-6xl font-semibold leading-tight">
              Crypto Dispatches
            </h1>
            <p className="text-lg text-white/70 leading-relaxed max-w-2xl">
              Deep dives on protocols, governance, and market structure—straight from verified
              wallets. Read freely, then connect to publish or react.
            </p>
            <div className="flex flex-col sm:flex-row gap-4 items-start">
              <div className="w-full sm:w-auto min-w-[240px]">
                <AuthButton />
              </div>
              <p className="text-xs uppercase tracking-[0.4em] text-white/60 max-w-xs">
                Wallet login unlocks posting, liking, and commenting.
              </p>
            </div>
            <div className="flex flex-col sm:flex-row gap-3">
              <Link
                href="/"
                className="inline-flex items-center justify-center rounded-full border border-white/20 px-6 py-3 text-sm font-semibold uppercase tracking-[0.3em] text-white hover:bg-white/10"
              >
                ← Random category
              </Link>
            </div>
          </div>
        </section>

        <div className="w-full px-6 pb-16 space-y-16">
          <div className="mx-auto max-w-6xl space-y-16">
            <PostSection
              title="Signal-Rich Crypto Posts"
              subtitle="Macro theses, protocol updates, tokenomics breakdowns, and governance takes from trusted wallets."
              posts={cryptoPosts}
              accent="crypto"
            />
          </div>
        </div>
      </Page.Main>
    </Page>
  );
}
