import { AuthButton } from '@/components/AuthButton';
import { Page } from '@/components/PageLayout';
import { PostSection } from '@/components/PostSection';
import { fetchPosts, filterPostsByTheme, type PostSummary } from '@/lib/posts';

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

export default async function MainPage() {
  const posts = await loadVisiblePosts();
  const randomPosts = filterPostsByTheme(posts, 'random');
  const cryptoPosts = filterPostsByTheme(posts, 'crypto');

  return (
    <Page className="bg-black text-white">
      <Page.Main className="!p-0 flex flex-col gap-16 bg-black text-white">
        <section className="w-full border-b border-white/10 bg-gradient-to-b from-black via-black to-[#04060a] px-6 py-16">
          <div className="mx-auto max-w-6xl grid gap-10 md:grid-cols-[1.4fr_1fr]">
            <div className="space-y-6">
              <p className="text-xs uppercase tracking-[0.4em] text-white/60">
                Open community • Wallet optional for browsing
              </p>
              <h1 className="text-5xl md:text-6xl font-semibold leading-tight">
                World Community Feed
              </h1>
              <p className="text-lg text-white/70 leading-relaxed max-w-2xl">
                Explore transparent conversations across Random musings and Crypto deep dives.
                Wallet login is only required when you want to post, comment, or react.
              </p>
              <div className="flex flex-col sm:flex-row gap-4 items-start">
                <div className="w-full sm:w-auto min-w-[240px]">
                  <AuthButton />
                </div>
                <p className="text-xs uppercase tracking-[0.4em] text-white/60 max-w-xs">
                  Viewing is open. Signing in lets you publish new takes backed by your World ID.
                </p>
              </div>
            </div>
            <div className="rounded-3xl border border-white/10 bg-white/5 p-6 space-y-4">
              <p className="text-sm uppercase tracking-[0.4em] text-white/60">
                Category signals
              </p>
              <div className="space-y-4">
                <div>
                  <p className="text-3xl font-semibold">{randomPosts.length}</p>
                  <p className="text-sm text-white/60">Random posts live</p>
                </div>
                <div>
                  <p className="text-3xl font-semibold">{cryptoPosts.length}</p>
                  <p className="text-sm text-white/60">Crypto posts streaming</p>
                </div>
              </div>
              <p className="text-xs text-white/50">
                Data refreshes whenever this page loads so you&apos;re always seeing the latest threads.
              </p>
            </div>
          </div>
        </section>

        <div className="w-full px-6 pb-16 space-y-16">
          <div className="mx-auto max-w-6xl space-y-16">
            <PostSection
              title="Offbeat Random Threads"
              subtitle="Serendipitous notes from verifiable humans—quick sketches, cultural snapshots, and in-between moments."
              posts={randomPosts}
              accent="random"
            />
            <PostSection
              title="On-chain Crypto Dispatches"
              subtitle="Signal-rich breakdowns on coins, protocols, and governance from wallet-native authors."
              posts={cryptoPosts}
              accent="crypto"
            />
          </div>
        </div>
      </Page.Main>
    </Page>
  );
}
